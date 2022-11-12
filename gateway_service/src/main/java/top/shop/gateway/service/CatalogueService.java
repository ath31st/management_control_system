package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private CatalogueDto catalogueFromStorage;
    private final RestTemplate restTemplate;

    public CatalogueDto getCatalogueFromStorage() {
        if (catalogueFromStorage == null)
            catalogueFromStorage = CatalogueDto.builder()
                    .products(Collections.emptyList())
                    .catalogueOnDate(LocalDateTime.now())
                    .build();

        return catalogueFromStorage;
    }

    public void setCatalogueFromStorage(CatalogueDto catalogueDto) {
        catalogueFromStorage = catalogueDto;
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
