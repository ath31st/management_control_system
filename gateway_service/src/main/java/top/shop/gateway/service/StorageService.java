package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.product.ProductDto;
import top.shop.gateway.util.wrapper.ProductWrapper;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;
    private final Keycloak keycloak;

    public ProductWrapper getProductWrapper() {
        String url = backendUrl + "/api/products";

        // TODO rework this block!
        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getToken());

        return restTemplate.exchange(RequestEntity.get(url).headers(headers).build(), ProductWrapper.class).getBody();
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
