package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.product.ProductServiceNameDto;
import top.shop.gateway.util.TokenExtractor;
import top.shop.gateway.util.wrapper.ProductPricingWrapper;
import top.shop.gateway.util.wrapper.ProductWrapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {
    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public CatalogueDto getCatalogueFromStorage(String shopServiceName) {
        String url = backendUrl + "/api/catalogue/" + shopServiceName;

        try {
            return restTemplate.exchange(RequestEntity.get(url)
                    .headers(TokenExtractor.headersWithTokenAuthUser())
                    .build(), CatalogueDto.class).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404)
                return CatalogueDto.builder()
                        .products(Collections.emptyList())
                        .catalogueOnDate(LocalDateTime.now())
                        .shopServiceName(shopServiceName)
                        .build();

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CatalogueDto getCatalogueFromShop(String shopUrl) {
        String url = shopUrl + "/api/manager/catalogue/";

        try {
            return restTemplate.exchange(RequestEntity.get(url)
                    .headers(TokenExtractor.headersWithTokenAuthUser())
                    .build(), CatalogueDto.class).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404)
                return CatalogueDto.builder()
                        .products(Collections.emptyList())
                        .catalogueOnDate(LocalDateTime.now())
                        .shopServiceName("NOT_FOUND")
                        .build();

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CatalogueDto prepareCatalogue(String shopServiceName, String[] productServiceName, ProductWrapper productWrapper) {
        return CatalogueDto.builder()
                .shopServiceName(shopServiceName)
                .catalogueOnDate(LocalDateTime.now())
                .products(productWrapper.getProductDtoList()
                        .stream()
                        .filter(p -> productServiceName != null && Arrays.stream(productServiceName)
                                .anyMatch(n -> p.getServiceName().equals(n)))
                        .toList())
                .build();
    }

    public void sendCatalogueToShop(CatalogueDto catalogueDto, String shopUrl) {
        String url = shopUrl + "/api/manager/catalogue";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(catalogueDto), CatalogueDto.class);
    }

    public void sendCatalogueChangesToShop(String shopServiceName, String shopUrl, String[] add, String[] delete) {
        ProductServiceNameDto productServiceNameDto = new ProductServiceNameDto();
        productServiceNameDto.setShopServiceName(shopServiceName);

        if (add != null) {
            productServiceNameDto.setAddProductServiceNames(List.of(add));
        } else {
            productServiceNameDto.setAddProductServiceNames(Collections.emptyList());
        }

        if (delete != null) {
            productServiceNameDto.setDeleteProductServiceNames(List.of(delete));
        } else {
            productServiceNameDto.setDeleteProductServiceNames(Collections.emptyList());
        }

        String url = shopUrl + "/api/manager/catalogue-changes";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(productServiceNameDto), ProductServiceNameDto.class);
    }

    public ProductPricingWrapper getProductPricingWrapperFromShop(String shopUrl) {
        String url = shopUrl + "/api/manager/prices";
        try {
            return restTemplate.exchange(RequestEntity.get(url)
                    .headers(TokenExtractor.headersWithTokenAuthUser())
                    .build(), ProductPricingWrapper.class).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                ProductPricingWrapper wrapper = new ProductPricingWrapper();
                wrapper.setPricingDtoList(Collections.emptyList());
                return wrapper;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void sendProductPricingWrapperToShop(ProductPricingWrapper wrapper, String shopUrl) {
        String url = shopUrl + "/api/manager/prices";
        restTemplate.postForObject(url, TokenExtractor.httpEntityWithTokenAuthUser(wrapper), ProductPricingWrapper.class);
    }

}
