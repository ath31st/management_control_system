package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    private CatalogueDto catalogueFromStorage;
    private final ProductService productService;

    public CatalogueDto getCatalogueForCustomers() {
        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(productService.getProductsWithPrice())
                .build();
    }

    public CatalogueDto getCatalogueForManagers() {
        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(productService.getProducts())
                .build();
    }

    public void setCatalogueFromStorage(CatalogueDto catalogueDto) {
        catalogueFromStorage = catalogueDto;
    }

    public CatalogueDto getCatalogueFromStorage() {
        if (catalogueFromStorage == null) {
            catalogueFromStorage = new CatalogueDto();
            catalogueFromStorage.setCatalogueOnDate(LocalDateTime.now());
            catalogueFromStorage.setProducts(Collections.emptyList());
        }
        return catalogueFromStorage;
    }

}
