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

    public Category getCategory(CategoryDto dto) {
        if (categoryRepository.existsByServiceName(dto.getServiceName()))
            return categoryRepository.findByServiceName(dto.getServiceName());
        else {
            return categoryRepository.save(Category.builder()
                    .serviceName(dto.getServiceName())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .build());
        }
    }
}
