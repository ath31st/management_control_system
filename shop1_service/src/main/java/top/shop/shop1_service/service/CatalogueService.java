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

    private final ProductService productService;

    public CatalogueDto getCatalogueForCustomers() {

        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setCatalogueOnDate(LocalDateTime.now());
        catalogueDto.setProducts(productService.getProductsWithPrice());
        return catalogueDto;
    }
}
