package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.repository.OrderRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public void persistOrder(OrderDto OrderDto) {
        Order order = modelMapper.map(OrderDto, Order.class);
        order.setLocalDateTime(LocalDateTime.now());
        Order persistedOrder = orderRepository.save(order);

        log.info("order persisted {}", persistedOrder);
    }

}
