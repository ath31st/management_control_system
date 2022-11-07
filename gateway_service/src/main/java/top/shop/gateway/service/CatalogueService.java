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

    //TODO MAKE STORAGE FOR CATALOGUE

    private CatalogueDto catalogueFromStorage;
    private final RestTemplate restTemplate;

    public CatalogueDto getCatalogue() {
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

    public CatalogueDto getCatalogueFromShop() {
        String url = shop1Url + "/shop1/api/manager/catalogue";
        return restTemplate.getForObject(url, CatalogueDto.class);
    }

    public void sendPricesToShop(CatalogueDto catalogueDto) {
        String url = shop1Url + "/shop1/api/manager/catalogue";
        restTemplate.postForObject(url, catalogueDto, CatalogueDto.class);
    }

}
