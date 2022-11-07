package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

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

    public void receiveCatalogueFromBackend(CatalogueDto catalogueDto) {
        if (catalogueDto == null) return;

        catalogueDto.getProducts()
                .forEach(pDto -> {
                    if (productService.productExists(pDto.getName())) {
                        productService.updateAmountProduct(pDto);
                    } else {
                        productService.saveNewProduct(pDto);
                    }
                });
    }

    public CatalogueDto receiveCatalogueFromGateway(CatalogueDto catalogueDto) {
        catalogueDto.getProducts().forEach(productService::updatePriceProduct);
        catalogueDto.setCatalogueOnDate(LocalDateTime.now());
        return catalogueDto;
    }

}
