package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.DeliveryProducer;
import top.shop.backend.dto.DeliveryOrderDto;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.entity.Payment;
import top.shop.backend.exceptionhandler.exception.OrderServiceException;
import top.shop.backend.repository.OrderRepository;
import top.shop.backend.service.event.OrderEvent;
import top.shop.backend.util.OrderStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ShopService shopService;
    private final ProductService productService;
    private final ApplicationEventPublisher eventPublisher;
    private final DeliveryProducer deliveryProducer;
    private final ModelMapper modelMapper;

    public void persistOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .amount(orderDto.getAmount())
                .status(OrderStatus.CREATED)
                .payment(modelMapper.map(orderDto.getPaymentDto(), Payment.class))
                .orderDate(orderDto.getOrderDate())
                .customerName(orderDto.getCustomerName())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopServiceName()))
                .build();

        Order persistedOrder = orderRepository.save(order);

//        eventPublisher.publishEvent(new OrderEvent(persistedOrder));
//        eventPublisher.publishEvent(new BalanceEvent(persistedOrder));

        log.info("order received and persisted {}", persistedOrder);
    }

    @EventListener
    public void sendDelivery(OrderEvent event) {
        Order order = (Order) event.getSource();
        DeliveryOrderDto deliveryOrderDto = processingDelivery(order);

        try {
            deliveryProducer.sendMessage(deliveryOrderDto);
        } catch (JsonProcessingException e) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        log.info("delivery {} processed and send to {}", deliveryOrderDto, deliveryOrderDto.getShopServiceName());

        productService.reduceAmountProduct(order.getAmount(), order.getProductName());
    }

    private DeliveryOrderDto processingDelivery(Order order) {
        return DeliveryOrderDto.builder()
                .amount(order.getAmount())
                .customerName(order.getCustomerName())
                .shopServiceName(order.getShop().getServiceName())
                .shopName(order.getShop().getName())
                .productName(order.getProductName())
                .totalPrice(order.getPayment().getTotalPrice().doubleValue())
                .build();
    }

}
