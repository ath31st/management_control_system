package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.DeliveryOrderDto;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.exceptionhandler.exception.OrderServiceException;
import top.shop.backend.repository.OrderRepository;
import top.shop.backend.config.kafkaconfig.DeliveryProducer;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShopService shopService;
    private final ProductService productService;

    private final DeliveryProducer deliveryProducer;

    public void persistOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .totalPrice(productService.getTotalPrice(orderDto.getAmount(), orderDto.getProductName()))
                .amount(orderDto.getAmount())
                .isExecuted(false)
                .orderDate(orderDto.getOrderDate())
                .customerName(orderDto.getCustomerName())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopName()))
                .build();
        Order persistedOrder = orderRepository.save(order);

        log.info("order received and persisted {}", persistedOrder);
    }

    public List<Order> getUnexecutedOrders() {
        return orderRepository.findByIsExecutedFalse();
    }

    public void processingDelivery(Order order) {
        DeliveryOrderDto deliveryOrderDto = DeliveryOrderDto.builder()
                .amount(order.getAmount())
                .customerName(order.getCustomerName())
                .shopName(order.getShop().getName())
                .productName(order.getProductName())
                .totalPrice(order.getTotalPrice())
                .build();
        try {
            deliveryProducer.sendMessage(deliveryOrderDto);
        } catch (JsonProcessingException e) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        log.info("delivery {} processed and send to {}", deliveryOrderDto, deliveryOrderDto.getShopName());
    }

    public void setExecutedStatusOrder(Order order) {
        if (!order.isExecuted()) {
            order.setExecuted(true);
            order.setExecutionDate(LocalDateTime.now());

            orderRepository.save(order);
        }

    }
}
