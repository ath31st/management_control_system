package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.util.Producer;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Producer producer;

    public String createOrder(OrderDto orderDto) throws JsonProcessingException {
        return producer.sendMessage(orderDto);
    }
}
