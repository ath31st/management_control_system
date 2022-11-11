package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.ProductDto;
import top.shop.shop1_service.dto.ProductPricingDto;
import top.shop.shop1_service.entity.ProductPricing;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    private CatalogueDto catalogueFromStorage;
    private final ProductPricingService productPricingService;

    public CatalogueDto getCatalogueForCustomers() {
        List<ProductDto> updatedProducts = getCatalogueFromStorage().getProducts()
                .stream()
                .filter(p -> productPricingService.productPricingExists(p.getName()))
                .map(productPricingService::updatePriceOfProductDto)
                .toList();

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(updatedProducts)
                .build();
    }

    public void setCatalogueFromStorage(CatalogueDto catalogueDto) {
        catalogueFromStorage = catalogueDto;
    }

    public CatalogueDto getCatalogueFromStorage() {
        if (catalogueFromStorage == null)
            catalogueFromStorage = CatalogueDto.builder()
                    .products(Collections.emptyList())
                    .catalogueOnDate(LocalDateTime.now())
                    .build();

        return catalogueFromStorage;
    }

}
