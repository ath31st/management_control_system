package top.shop.shop1_service.service;

import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.OrderDto;

@Service
public class OrderService {

    public OrderDto newOrder(OrderDto orderDto) {
        return orderDto;
    }
}
