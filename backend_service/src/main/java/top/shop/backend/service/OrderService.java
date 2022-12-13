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
import top.shop.backend.dto.delivery.DeliveryOrderDto;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.entity.Order;
import top.shop.backend.entity.Payment;
import top.shop.backend.exceptionhandler.exception.OrderServiceException;
import top.shop.backend.repository.OrderRepository;
import top.shop.backend.service.event.BalanceEvent;
import top.shop.backend.service.event.PaymentEvent;
import top.shop.backend.util.DeliveryStatus;
import top.shop.backend.util.OrderStatus;

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
                .customerName(orderDto.getCustomerName())
                .productName(orderDto.getProductName())
                .shop(shopService.getShop(orderDto.getShopServiceName()))
                .build();

        Order persistedOrder = orderRepository.save(order);
        productService.reduceAmountProduct(order.getAmount(), order.getProductName());

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

    @EventListener
    public void processingOrder(PaymentEvent event) {
        Payment payment = (Payment) event.getSource();
        Order order = orderRepository.findByPayment_PaymentUuid(payment.getPaymentUuid());

        switch (payment.getPaymentStatus()) {
            case EXPIRED -> order.setStatus(OrderStatus.EXPIRED);
            case CANCELED -> order.setStatus(OrderStatus.CANCELED);
            case REJECTION -> order.setStatus(OrderStatus.REJECTED);
            case EXECUTED -> order.setStatus(OrderStatus.IS_PAID);
        }

        orderRepository.save(order);

        if (order.getStatus().equals(OrderStatus.IS_PAID)) {
            eventPublisher.publishEvent(new BalanceEvent(order));
            sendDelivery(order);
        }
    }

    public void processingDeliveryResult(DeliveryOrderDto dto) {
        Order order = getOrderById(dto.getOrderNumber());

        switch (dto.getDeliveryStatus()) {
            case DELIVERED -> order.setStatus(OrderStatus.DELIVERED);
            case REJECTED -> {
                productService.increaseAmountProduct(dto.getAmount(), dto.getProductName());
                shopService.moneyBackFromBalance(dto.getTotalPrice(), dto.getShopServiceName());
                paymentService.chargeBack(dto.getCustomerName(), dto.getTotalPrice());
                order.setStatus(OrderStatus.REJECTED);
            }
        }

        orderRepository.save(order);
    }

    private DeliveryOrderDto processingDelivery(Order order) {
        return DeliveryOrderDto.builder()
                .deliveryStatus(DeliveryStatus.IN_TRANSIT)
                .orderNumber(order.getId())
                .amount(order.getAmount())
                .customerName(order.getCustomerName())
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
