package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public List<CategoryDto> getCategories() {
        String url = backendUrl + "/api/categories";
        CategoryDto[] categories = restTemplate.getForObject(url, CategoryDto[].class);

        if (categories == null) return new ArrayList<>();

        return new ArrayList<>(List.of(categories));
    }

    public CategoryDto getCategory(String categoryServiceName) {
        String url = backendUrl + "/api/categories/" + categoryServiceName;
       return restTemplate.getForObject(url, CategoryDto.class);
    }

    public void createCategory(CategoryDto categoryDto) {
        String url = backendUrl + "/api/new-category";
        restTemplate.postForObject(url, categoryDto, CategoryDto.class);
    }

    public void sendCategoryChanges(CategoryDto categoryDto) {
        String url = backendUrl + "/api/edit-category";
        restTemplate.postForObject(url, categoryDto, CategoryDto.class);
    }

}
