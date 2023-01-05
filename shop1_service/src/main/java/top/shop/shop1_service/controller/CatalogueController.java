package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductServiceNameDto;
import top.shop.shop1_service.service.CatalogueService;
import top.shop.shop1_service.service.ProductPricingService;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogueController {
    private final CatalogueService catalogueService;
    private final ProductPricingService productPricingService;

    @GetMapping("/catalogue")
    public ResponseEntity<CatalogueDto> catalogueForCustomersHandler() {
        return ResponseEntity.ok(catalogueService.getCatalogueForCustomers());
    }

    @GetMapping("/manager/catalogue")
    public ResponseEntity<CatalogueDto> catalogue() {
        return ResponseEntity.ok(catalogueService.getCatalogueDto());
    }

    @PostMapping("/manager/catalogue")
    public ResponseEntity<HttpStatus> catalogueHandler(@RequestBody CatalogueDto catalogueDto) {
        catalogueService.saveCatalogueFromDto(catalogueDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/manager/catalogue-changes")
    public ResponseEntity<HttpStatus> catalogueChangesHandler(@RequestBody ProductServiceNameDto productServiceNameDto) {
        catalogueService.updateCatalogue(productServiceNameDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manager/prices")
    public ResponseEntity<ProductPricingWrapper> catalogueForGatewayHandler() {
        return ResponseEntity.ok(new ProductPricingWrapper(
                productPricingService.getProductPricingDtoList(catalogueService.getProductServiceNameList())));
    }

    @PostMapping("/manager/prices")
    public ResponseEntity<ProductPricingWrapper> catalogueForGatewayHandler(@RequestBody ProductPricingWrapper wrapper) {
        productPricingService.receiveProductPricingWrapperFromGateway(wrapper);
        return ResponseEntity.ok().build();
    }

}
