package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.dto.ProductPricingDto;
import top.shop.shop1_service.service.CatalogueService;
import top.shop.shop1_service.service.OrderService;
import top.shop.shop1_service.service.ProductPricingService;

import java.util.List;

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
    public ResponseEntity<List<ProductPricingDto>> catalogueForGatewayHandler() {
        return ResponseEntity.ok(productPricingService.getProductPricingDtoList());
    }

    @PostMapping("/manager/prices")
    public ResponseEntity<List<ProductPricingDto>> catalogueForGatewayHandler(@RequestBody List<ProductPricingDto> ppDto) {
        productPricingService.receiveProductPricingFromGateway(ppDto);
        return ResponseEntity.ok().build();
    }

}
