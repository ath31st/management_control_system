package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.ProductPricingDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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


    public List<ProductPricingDto> getProductPricingListFromShop(String shopUrl) {
        String url = shopUrl + "/api/manager/prices";

        return List.of(Objects.requireNonNull(restTemplate.getForObject(url, ProductPricingDto[].class)));
    }

    public void sendProductPricingListToShop(List<ProductPricingDto> pricingDtoList, String shopUrl) {
        String url = shopUrl + "/api/manager/prices";
        restTemplate.postForObject(url, pricingDtoList, ProductPricingDto[].class);
    }

}
