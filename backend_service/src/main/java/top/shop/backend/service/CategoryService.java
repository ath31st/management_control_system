package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.CategoryWrapperProducer;
import top.shop.backend.dto.CategoryDto;
import top.shop.backend.entity.Category;
import top.shop.backend.exceptionhandler.exception.CategoryException;
import top.shop.backend.repository.CategoryRepository;
import top.shop.backend.util.wrapper.CategoryWrapper;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryWrapperProducer categoryWrapperProducer;
    private final ModelMapper modelMapper;
    private final static String DEFAULT_CATEGORY = "default_category";

    public Category getCategory(String categoryServiceName) {
        return categoryRepository.findByServiceName(categoryServiceName).orElseThrow(
                () -> new CategoryException(HttpStatus.NOT_FOUND, "Category with name " + categoryServiceName + " not found!"));
    }

    public CategoryDto getCategoryDto(String categoryServiceName) {
        Category category = getCategory(categoryServiceName);
        return modelMapper.map(category, CategoryDto.class);
    }

    public List<CategoryDto> getListCategoryDto() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(c -> modelMapper.map(c, CategoryDto.class))
                .toList();
    }

    public Category saveCategory(CategoryDto categoryDto) {
        Category category = Category.builder()
                .name(categoryDto.getName())
                .serviceName(categoryDto.getServiceName())
                .description(categoryDto.getDescription())
                .products(new HashSet<>())
                .build();

        return categoryRepository.save(category);
    }

    public void saveChangesCategory(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getServiceName());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        CategoryDto dto = modelMapper.map(categoryRepository.save(category), CategoryDto.class);
        CategoryWrapper wrapper = new CategoryWrapper();
        wrapper.setUpdatedCategory(dto);
        try {
            categoryWrapperProducer.sendMessage(wrapper);
        } catch (JsonProcessingException e) {
            throw new CategoryException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public void deleteCategory(String categoryServiceName) {
        Category category = getCategory(categoryServiceName);

        categoryRepository.delete(category);

        CategoryWrapper wrapper = new CategoryWrapper();
        wrapper.setDeletedCategory(categoryServiceName);
        wrapper.setReplacementCategory(getCategoryDto(DEFAULT_CATEGORY));
        try {
            categoryWrapperProducer.sendMessage(wrapper);
        } catch (JsonProcessingException e) {
            throw new CategoryException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public boolean categoryExists(String categoryServiceName) {
        return categoryRepository.existsByServiceName(categoryServiceName);
    }

}
