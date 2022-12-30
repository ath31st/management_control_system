package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.DeliveryResultProducer;
import top.shop.shop1_service.dto.delivery.DeliveryOrderDto;
import top.shop.shop1_service.entity.DeliveryOrder;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.exceptionhandler.exception.DeliveryServiceException;
import top.shop.shop1_service.repository.DeliveryOrderRepository;
import top.shop.shop1_service.util.DeliveryStatus;

import java.util.ArrayList;
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

//    public DeliveryStatus acceptingDelivery(String orderUuidNumber, boolean isAccept) {
//        DeliveryOrder deliveryOrder = mongoTemplate.findById(orderUuidNumber, DeliveryOrder.class);
//        if (deliveryOrder == null) return DeliveryStatus.NOT_FOUND;
//        if (deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.DELIVERED) |
//                deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.REJECTED)) {
//            throw new DeliveryServiceException(HttpStatus.BAD_REQUEST, "Order with number " + orderUuidNumber + " already closed!");
//        }
//
//        if (isAccept) {
//            deliveryOrder.setDeliveryStatus(DeliveryStatus.DELIVERED);
//        } else {
//            deliveryOrder.setDeliveryStatus(DeliveryStatus.REJECTED);
//        }
//
//        mongoTemplate.save(deliveryOrder);
//
//        try {
//            deliveryResultProducer.sendMessage(modelMapper.map(deliveryOrder, DeliveryOrderDto.class));
//        } catch (JsonProcessingException e) {
//            throw new DeliveryServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//
//        return deliveryOrder.getDeliveryStatus();
//    }

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
        deliveryOrder.setProducts(List.of(productService.getProduct(dto.getProductName())));
        deliveryOrder.setPayment(paymentService.getPayment(dto.getOrderUuidNumber()));
        deliveryOrder.setCustomer(customerService.getCustomer(dto.getCustomerName()));

        return deliveryOrderRepository.save(deliveryOrder);
    }
}
