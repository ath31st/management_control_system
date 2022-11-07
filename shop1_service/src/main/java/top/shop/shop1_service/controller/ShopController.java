package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.service.CatalogueService;
import top.shop.shop1_service.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/shop1/api")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;
    private final CatalogueService catalogueService;

    @PostMapping("/order")
    public String orderHandler(@RequestBody OrderDto orderDto) {
        log.info("create food order request received");
        return orderService.createOrder(orderDto);
    }

    @GetMapping("/catalogue")
    public ResponseEntity<CatalogueDto> catalogueHandler() {
        return ResponseEntity.ok(catalogueService.getCatalogueForCustomers());
    }
}
