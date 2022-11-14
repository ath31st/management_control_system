package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.util.wrapper.ProductPricingWrapper;

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

        CatalogueDto catalogueFromStorage = restTemplate.getForObject(url, CatalogueDto.class);

        if (catalogueFromStorage == null)
            return CatalogueDto.builder()
                    .products(Collections.emptySet())
                    .catalogueOnDate(LocalDateTime.now())
                    .shopServiceName(shopNameService)
                    .build();

        return catalogueFromStorage;
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
