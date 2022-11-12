package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.service.CatalogueService;
import top.shop.shop1_service.service.OrderService;
import top.shop.shop1_service.service.ProductPricingService;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final CatalogueService catalogueService;
    private final ProductPricingService productPricingService;

    @PostMapping("/order")
    public String orderHandler(@RequestBody OrderDto orderDto) {
        log.info("create food order request received");
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/catalogue")
    public ResponseEntity<CatalogueDto> catalogueForCustomersHandler() {
        return ResponseEntity.ok(catalogueService.getCatalogueForCustomers());
    }

    @GetMapping("/manager/prices")
    public ResponseEntity<ProductPricingWrapper> catalogueForGatewayHandler() {
        return ResponseEntity.ok(new ProductPricingWrapper(productPricingService.getProductPricingDtoList()));
    }

    @PostMapping("/manager/prices")
    public ResponseEntity<ProductPricingWrapper> catalogueForGatewayHandler(@RequestBody ProductPricingWrapper wrapper) {
        productPricingService.receiveProductPricingWrapperFromGateway(wrapper);
        return ResponseEntity.ok().build();
    }

}
