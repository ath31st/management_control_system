package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CategoryDto;
import top.shop.shop1_service.entity.Category;
import top.shop.shop1_service.repository.CategoryRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category categoryDtoToCategoryConverter(CategoryDto dto) {
        if (existsCategory(dto.getServiceName()))
            return categoryRepository.findByServiceName(dto.getServiceName());
        else {
            return categoryRepository.save(Category.builder()
                    .serviceName(dto.getServiceName())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .build());
        }
    }

    public Category getCategory(String categoryServiceName) {
        return categoryRepository.findByServiceName(categoryServiceName);
    }

    public CategoryDto getCategoryDto(String categoryServiceName) {
        Category c = getCategory(categoryServiceName);
        CategoryDto dto = new CategoryDto();
        dto.setName(c.getName());
        dto.setServiceName(c.getServiceName());
        dto.setDescription(c.getDescription());
        return dto;
    }

    public CategoryDto categoryToDtoConverter(Category c) {
        CategoryDto dto = new CategoryDto();
        dto.setName(c.getName());
        dto.setServiceName(c.getServiceName());
        dto.setDescription(c.getDescription());
        return dto;
    }

    public void updateCategory(CategoryDto dto) {
        Category category = getCategory(dto.getServiceName());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        categoryRepository.save(category);
    }

    public void deleteCategory(String categoryServiceName) {
        Category category = getCategory(categoryServiceName);

        categoryRepository.delete(category);
    }

    public boolean existsCategory(String categoryServiceName) {
        return categoryRepository.existsByServiceName(categoryServiceName);
    }
}
