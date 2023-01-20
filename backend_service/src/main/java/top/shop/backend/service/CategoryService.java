package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.CategoryDto;
import top.shop.backend.entity.Category;
import top.shop.backend.exceptionhandler.exception.CategoryException;
import top.shop.backend.repository.CategoryRepository;
import top.shop.backend.service.event.CategoryEvent;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

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

    public Category saveChangesCategory(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getServiceName());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    public void deleteCategory(String categoryServiceName) {
        Category category = getCategory(categoryServiceName);

        categoryRepository.delete(category);
    }

    public boolean categoryExists(String categoryServiceName) {
        return categoryRepository.existsByServiceName(categoryServiceName);
    }

}
