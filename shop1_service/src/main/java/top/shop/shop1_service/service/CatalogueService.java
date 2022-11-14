package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${shop.service-name}")
    private String serviceName;

    private CatalogueDto catalogueFromStorage;
    private final ProductPricingService productPricingService;

    public CatalogueDto getCatalogueForCustomers() {
        Set<ProductDto> updatedProducts = getCatalogueFromStorage().getProducts()
                .stream()
                .filter(p -> productPricingService.productPricingExists(p.getServiceName()) &&
                        productPricingService.getProductPricingDto(p.getServiceName()).getPrice() != 0)
                .map(productPricingService::updatePriceOfProductDto)
                .collect(Collectors.toSet());

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(updatedProducts)
                .shopServiceName(catalogueFromStorage.getShopServiceName())
                .build();
    }

    public void setCatalogueFromStorage(CatalogueDto catalogueDto) {
        catalogueFromStorage = catalogueDto;
        productPricingService.addMockProductPricing(catalogueDto);
    }

    public CatalogueDto getCatalogueFromStorage() {
        if (catalogueFromStorage == null)
            catalogueFromStorage = CatalogueDto.builder()
                    .products(Collections.emptySet())
                    .catalogueOnDate(LocalDateTime.now())
                    .shopServiceName(serviceName)
                    .build();

        return catalogueFromStorage;
    }

}
