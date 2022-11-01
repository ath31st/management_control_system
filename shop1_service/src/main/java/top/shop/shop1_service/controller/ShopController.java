package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.service.OrderService;

@RestController
@RequestMapping("/shop1/api")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;

    @PostMapping("/new-order")
    public ResponseEntity<OrderDto> orderHandler(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.newOrder(orderDto));
    }
}
