package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.CategoryDto;
import top.shop.gateway.util.wrapper.ProductPricingWrapper;

import javax.xml.catalog.CatalogException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${backend.url}")
    private String backendUrl;
    private final RestTemplate restTemplate;

    public CatalogueDto getCatalogueFromStorage(String shopNameService) {
        String url = backendUrl + "/api/catalogue/" + shopNameService;

        try {
            return restTemplate.getForObject(url, CatalogueDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404)
                return CatalogueDto.builder()
                        .products(Collections.emptySet())
                        .catalogueOnDate(LocalDateTime.now())
                        .shopServiceName(shopNameService)
                        .build();

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendCatalogueToStorage(CatalogueDto catalogueDto) {
        String url = backendUrl + "/api/catalogue";
        restTemplate.postForObject(url, catalogueDto, CatalogueDto.class);
    }

    public ProductPricingWrapper getProductPricingWrapperFromShop(String shopUrl) {
        String url = shopUrl + "/api/manager/prices";

        return restTemplate.getForObject(url, ProductPricingWrapper.class);
    }

    public void sendProductPricingWrapperToShop(ProductPricingWrapper wrapper, String shopUrl) {
        String url = shopUrl + "/api/manager/prices";
        restTemplate.postForObject(url, wrapper, ProductPricingWrapper.class);
    }

}
