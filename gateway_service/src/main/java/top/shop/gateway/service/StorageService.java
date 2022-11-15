package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.ProductDto;
import top.shop.gateway.util.wrapper.ProductWrapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public ProductWrapper getProductWrapper() {
        String url = backendUrl + "/api/products";

        return restTemplate.getForObject(url, ProductWrapper.class);
    }

    public ProductWrapper getProductWrapperWithoutCatalogue(CatalogueDto catalogueDto) {
        String url = backendUrl + "/api/products-without-catalogue";
        List<String> catalogueProductNames = catalogueDto.getProducts()
                .stream()
                .map(ProductDto::getServiceName)
                .toList();

        return restTemplate.postForObject(url, catalogueProductNames, ProductWrapper.class);
    }

    public void sendProductWrapperToBackend(ProductWrapper wrapper) {
        String url = backendUrl + "/api/products";
        wrapper.setProductDtoList(filterUnchangedProducts(wrapper.getProductDtoList()));

        restTemplate.postForObject(url, wrapper, ProductWrapper.class);
    }

    public void createProduct(ProductDto productDto) {
        String url = backendUrl + "/api/products";

        restTemplate.postForObject(url, new ProductWrapper(Collections.singletonList(productDto)), ProductWrapper.class);
    }

    private List<ProductDto> filterUnchangedProducts(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .filter(p -> p.getAmount() != 0)
                .toList();
    }
}
