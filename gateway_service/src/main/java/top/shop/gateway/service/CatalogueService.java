package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.gateway.dto.CatalogueDto;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${shop1.url}")
    private String shop1Url;

    private CatalogueDto catalogueFromStorage;
    private final RestTemplate restTemplate;

    public CatalogueDto getCatalogueFromStorage() {
        if (catalogueFromStorage == null) {
            catalogueFromStorage = new CatalogueDto();
            catalogueFromStorage.setCatalogueOnDate(LocalDateTime.now());
            catalogueFromStorage.setProducts(Collections.emptyList());
        }
        return catalogueFromStorage;
    }

    public void setCatalogueFromStorage(CatalogueDto catalogueDto) {
        catalogueFromStorage = catalogueDto;
    }

    public CatalogueDto getCatalogueFromShop(String shopUrl) {
        String url = shopUrl + "/api/manager/catalogue";
        return restTemplate.getForObject(url, CatalogueDto.class);
    }

    public void sendPricesToShop(CatalogueDto catalogueDto, String shopUrl) {
        String url = shopUrl + "/api/manager/catalogue";
        restTemplate.postForObject(url, catalogueDto, CatalogueDto.class);
    }

}
