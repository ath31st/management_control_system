package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.DeliveryProducer;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.dto.delivery.DeliveryOrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.entity.Payment;
import top.shop.backend.exceptionhandler.exception.OrderServiceException;
import top.shop.backend.repository.OrderRepository;
import top.shop.backend.service.event.BalanceEvent;
import top.shop.backend.service.event.OrderEvent;
import top.shop.backend.util.DeliveryStatus;
import top.shop.backend.util.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
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
                .customerEmail(orderDto.getCustomerEmail())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopServiceName()))
                .build();

        Order persistedOrder = orderRepository.save(order);
        productService.reduceAmountProduct(order.getAmount(), order.getProductName());

        // add order to scheduler list
        eventPublisher.publishEvent(new OrderEvent(order));
        log.info("order received and persisted {}", persistedOrder);
    }

    public void sendDelivery(Order order) {
        DeliveryOrderDto deliveryOrderDto = processingDelivery(order);

        try {
            deliveryProducer.sendMessage(deliveryOrderDto);
        } catch (JsonProcessingException e) {
            throw new OrderServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        log.info("delivery {} processed and send to {}", deliveryOrderDto, deliveryOrderDto.getShopServiceName());
    }

    public void processingOrder(Payment payment) {
        Order order = orderRepository.findByPayment_PaymentUuid(payment.getPaymentUuid());

        switch (payment.getPaymentStatus()) {
            case EXPIRED -> order.setStatus(OrderStatus.EXPIRED);
            case CANCELED -> order.setStatus(OrderStatus.CANCELED);
            case REJECTION -> order.setStatus(OrderStatus.REJECTED);
            case EXECUTED -> order.setStatus(OrderStatus.IS_PAID);
        }

        orderRepository.save(order);
        // remove order from scheduler list
        eventPublisher.publishEvent(new OrderEvent(order));

        if (order.getStatus().equals(OrderStatus.IS_PAID)) {
            eventPublisher.publishEvent(new BalanceEvent(order));
            sendDelivery(order);
        }
    }

    public Order saveOrderChanges(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return new ArrayList<>(orderRepository.findByStatus(status));
    }

    public void processingDeliveryResult(DeliveryOrderDto dto) {
        Order order = orderRepository.findByPayment_PaymentUuid(dto.getOrderUuidNumber());

        switch (dto.getDeliveryStatus()) {
            case DELIVERED -> order.setStatus(OrderStatus.DELIVERED);
            case REJECTED -> {
                productService.increaseAmountProduct(dto.getAmount(), dto.getProductName());
                shopService.moneyBackFromBalance(dto.getTotalPrice(), dto.getShopServiceName());
                paymentService.chargeBack(dto.getCustomerEmail(), dto.getTotalPrice());
                order.setStatus(OrderStatus.REJECTED);
            }
        }

        order.setExecutionDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    private DeliveryOrderDto processingDelivery(Order order) {
        return DeliveryOrderDto.builder()
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .orderUuidNumber(order.getPayment().getPaymentUuid())
                .amount(order.getAmount())
                .customerEmail(order.getCustomerEmail())
                .shopServiceName(order.getShop().getServiceName())
                .shopName(order.getShop().getName())
                .productName(order.getProductName())
                .totalPrice(order.getPayment().getTotalPrice().doubleValue())
                .build();
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new OrderServiceException(HttpStatus.NOT_FOUND, "Order with id " + id + " not found"));
    }

}
