package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.product.ProductDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.ProductWrapper;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public ProductWrapper getProductWrapper() {
        String url = backendUrl + "/api/products";

        return restTemplate.exchange(RequestEntity.get(url)
                        .headers(TokenExtractor.headersWithTokenAuthUser())
                        .build(), ProductWrapper.class).getBody();
    }

    public ProductDto getProductDto(String productServiceName) {
        String url = backendUrl + "/api/products/" + productServiceName;

        return restTemplate.exchange(RequestEntity.get(url)
                .headers(TokenExtractor.headersWithTokenAuthUser())
                .build(), ProductDto.class).getBody();
    }

    public ProductWrapper getProductWrapperWithoutCatalogue(CatalogueDto catalogueDto) {
        String url = backendUrl + "/api/products-without-catalogue";
        List<String> catalogueProductNames = catalogueDto.getProducts()
                .stream()
                .map(ProductDto::getServiceName)
                .toList();

        return restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(catalogueProductNames), ProductWrapper.class);
    }

    public void sendProductWrapperToBackend(ProductWrapper wrapper) {
        String url = backendUrl + "/api/products";
        wrapper.setProductDtoList(filterUnchangedProducts(wrapper.getProductDtoList()));

        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(wrapper), ProductWrapper.class);
    }

    public void createProduct(ProductDto productDto) {
        String url = backendUrl + "/api/products";
        ProductWrapper pw = new ProductWrapper(Collections.singletonList(productDto));
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(pw), ProductWrapper.class);
    }

    private List<ProductDto> filterUnchangedProducts(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .filter(p -> p.getAmount() != 0)
                .toList();
    }
}
