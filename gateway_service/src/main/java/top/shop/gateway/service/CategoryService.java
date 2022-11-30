package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CategoryDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.ProductWrapper;

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

        CategoryDto[] categories = restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), CategoryDto[].class).getBody();

        if (categories == null) return new ArrayList<>();

        return new ArrayList<>(List.of(categories));
    }

    public CategoryDto getCategory(String categoryServiceName) {
        String url = backendUrl + "/api/categories/" + categoryServiceName;
        return restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), CategoryDto.class).getBody();
    }

    public void createCategory(CategoryDto categoryDto) {
        String url = backendUrl + "/api/new-category";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(categoryDto), CategoryDto.class);
    }

    public void sendCategoryChanges(CategoryDto categoryDto) {
        String url = backendUrl + "/api/edit-category";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(categoryDto), CategoryDto.class);
    }

    public void deleteCategory(String categoryServiceName) {
        String url = backendUrl + "/api/delete-category/" + categoryServiceName;
        restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), String.class).getBody();
    }

}
