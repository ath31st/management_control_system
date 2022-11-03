package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.service.kafkalogic.OrderProducer;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${shop.name}")
    private final String shopName = "shop1";
    private final OrderProducer orderProducer;

    public String createOrder(OrderDto orderDto) throws JsonProcessingException {
        orderDto.setShopName(shopName);
        orderDto.setOrderDate(LocalDateTime.now());
        return orderProducer.sendMessage(orderDto);
    }

}
