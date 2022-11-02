package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShopService shopService;

    public void persistOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .isExecuted(false)
                .orderDate(orderDto.getOrderDate())
                .customerName(orderDto.getCustomerName())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopName()))
                .build();
        Order persistedOrder = orderRepository.save(order);

        log.info("order received and persisted {}", persistedOrder);
    }

}
