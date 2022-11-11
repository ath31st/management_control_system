package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.CategoryDto;
import top.shop.backend.entity.Category;
import top.shop.backend.exceptionhandler.exception.CategoryException;
import top.shop.backend.repository.CategoryRepository;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public Category getCategory(String categoryName) {
        return categoryRepository.findByName(categoryName).orElseThrow(
                () -> new CategoryException(HttpStatus.NOT_FOUND, "Category with name " + categoryName + " not found!"));
    }

    public CategoryDto getCategoryDto(String categoryName) {
        Category category = getCategory(categoryName);
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
                .description(categoryDto.getDescription())
                .products(new HashSet<>())
                .build();

        return categoryRepository.save(category);
    }

    public Category saveChangesCategory(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    public boolean categoryExists(String categoryName) {
        return categoryRepository.existsByName(categoryName);
    }

}
