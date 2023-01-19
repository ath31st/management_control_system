package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.DeliveryResultProducer;
import top.shop.shop1_service.dto.delivery.DeliveryOrderDto;
import top.shop.shop1_service.entity.DeliveryOrder;
import top.shop.shop1_service.exceptionhandler.exception.DeliveryServiceException;
import top.shop.shop1_service.repository.DeliveryOrderRepository;
import top.shop.shop1_service.util.DeliveryStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryResultProducer deliveryResultProducer;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final CustomerService customerService;

    public void receiveDeliveryFromStorage(DeliveryOrderDto deliveryOrderDto) {

        deliveryOrderDto.setDeliveryStatus(DeliveryStatus.READY_FOR_RECIPIENT);

        DeliveryOrder deliveryOrder = saveDeliveryFromDto(deliveryOrderDto);
        paymentService.setDeliveryOrderInPayment(deliveryOrder);
        //TODO here need email notification customer - order delivered
    }

    public DeliveryStatus acceptingDelivery(String orderUuidNumber, boolean isAccept) {
        DeliveryOrder deliveryOrder = getDeliveryOrder(orderUuidNumber);
        if (deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.DELIVERED) |
                deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.REJECTED)) {
            throw new DeliveryServiceException(HttpStatus.BAD_REQUEST, "Order with number " + orderUuidNumber + " already closed!");
        }

        if (isAccept) {
            deliveryOrder.setDeliveryStatus(DeliveryStatus.DELIVERED);
        } else {
            deliveryOrder.setDeliveryStatus(DeliveryStatus.REJECTED);
        }

        deliveryOrderRepository.save(deliveryOrder);

        try {
            deliveryResultProducer.sendMessage(deliveryOrderToDto(deliveryOrder));
        } catch (JsonProcessingException e) {
            throw new DeliveryServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return deliveryOrder.getDeliveryStatus();
    }

    public DeliveryOrder getDeliveryOrder(String orderUuidNumber) {
        return deliveryOrderRepository.findByOrderUuidNumber(orderUuidNumber).orElseThrow(
                () -> new DeliveryServiceException(HttpStatus.NOT_FOUND, "Delivery with UUID: " + orderUuidNumber + " no found."));
    }

    public DeliveryStatus checkDeliveryStatus(String orderUuidNumber) {
        if (deliveryOrderRepository.existsByOrderUuidNumber(orderUuidNumber)) {
            return getDeliveryOrder(orderUuidNumber).getDeliveryStatus();
        } else {
            return DeliveryStatus.NOT_FOUND;
        }
    }

    public DeliveryOrder saveDeliveryFromDto(DeliveryOrderDto dto) {
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderUuidNumber(dto.getOrderUuidNumber());
        deliveryOrder.setShopServiceName(dto.getShopServiceName());
        deliveryOrder.setShopName(dto.getShopName());
        deliveryOrder.setAmount(dto.getAmount());
        deliveryOrder.setTotalPrice(dto.getTotalPrice());
        deliveryOrder.setDeliveryStatus(dto.getDeliveryStatus());
        deliveryOrder.setProducts(Collections.singletonList(productService.getProduct(dto.getProductServiceName())));
        deliveryOrder.setPayment(paymentService.getPayment(dto.getOrderUuidNumber()));
        deliveryOrder.setCustomer(customerService.getCustomer(dto.getCustomerEmail()));

        return deliveryOrderRepository.save(deliveryOrder);
    }

    public DeliveryOrderDto deliveryOrderToDto(DeliveryOrder order) {
        DeliveryOrderDto dto = new DeliveryOrderDto();
        dto.setOrderUuidNumber(order.getOrderUuidNumber());
        dto.setShopServiceName(order.getShopServiceName());
        dto.setShopName(order.getShopName());
        dto.setProductServiceName(order.getProducts().get(0).getServiceName());
        dto.setCustomerEmail(order.getCustomer().getEmail());
        dto.setAmount(order.getAmount());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setDeliveryStatus(order.getDeliveryStatus());

        return dto;
    }
}
