package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.DeliveryOrderDto;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.repository.OrderRepository;
import top.shop.backend.service.kafkalogic.DeliveryProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShopService shopService;
    private final ProductService productService;

    private final DeliveryProducer deliveryProducer;

    public void persistOrder(OrderDto orderDto) throws JsonProcessingException {
        Order order = Order.builder()
                .totalPrice(productService.getTotalPrice(orderDto.getAmount(),orderDto.getProductName()))
                .amount(orderDto.getAmount())
                .isExecuted(false)
                .orderDate(orderDto.getOrderDate())
                .customerName(orderDto.getCustomerName())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopName()))
                .build();
        Order persistedOrder = orderRepository.save(order);


        //TODO remove this block to standalone service
        DeliveryOrderDto deliveryOrderDto = new DeliveryOrderDto();
        deliveryOrderDto.setAmount(persistedOrder.getAmount());
        deliveryOrderDto.setCustomerName(persistedOrder.getCustomerName());
        deliveryOrderDto.setProductName(persistedOrder.getProductName());
        deliveryOrderDto.setTotalPrice(persistedOrder.getTotalPrice());
        deliveryOrderDto.setShopName(persistedOrder.getShop().getName());
        deliveryProducer.sendMessage(deliveryOrderDto);

        log.info("order received and persisted {}", persistedOrder);
    }

}
