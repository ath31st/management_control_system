package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.exceptionhandler.exception.OrderServiceException;
import top.shop.shop1_service.service.kafkalogic.OrderProducer;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${shop.name}")
    private String shopName;
    private final OrderProducer orderProducer;

    public String createOrder(OrderDto orderDto)  {
        orderDto.setShopName(shopName);
        orderDto.setOrderDate(LocalDateTime.now());

        try {
            return orderProducer.sendMessage(orderDto);
        } catch (JsonProcessingException e) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
