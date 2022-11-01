package top.shop.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import top.shop.gateway.dto.OrderDto;
import top.shop.gateway.entity.Order;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;

    public void persistOrder(OrderDto OrderDto) {
        Order order = modelMapper.map(OrderDto, Order.class);
        Order persistedOrder = order;

        log.info("order persisted {}", persistedOrder);
    }

}
