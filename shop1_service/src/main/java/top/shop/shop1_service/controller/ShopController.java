package top.shop.shop1_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/shop1/api")
@RequiredArgsConstructor
public class ShopController {

    private final OrderService orderService;

    @PostMapping("/order")
    public String orderHandler(@RequestBody OrderDto orderDto) throws JsonProcessingException {
        log.info("create food order request received");
        return orderService.createOrder(orderDto);
    }
}
