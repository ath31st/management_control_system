package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.dto.PaymentDto;
import top.shop.shop1_service.service.CatalogueService;
import top.shop.shop1_service.service.OrderService;
import top.shop.shop1_service.service.ProductPricingService;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final CatalogueService catalogueService;
    private final ProductPricingService productPricingService;

    @PostMapping("/order")
    public ResponseEntity<PaymentDto> orderHandler(@Valid @RequestBody OrderDto orderDto) {
        log.info("order request received");
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

//    @PostMapping("/payment")
//    public ResponseEntity<PaymentDto> orderHandler(@Valid @RequestBody PaymentDto paymentDto) {
//        log.info("payment request received");
//
//    }

    @GetMapping("/catalogue")
    public ResponseEntity<CatalogueDto> catalogueForCustomersHandler() {
        return ResponseEntity.ok(catalogueService.getCatalogueForCustomers());
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
