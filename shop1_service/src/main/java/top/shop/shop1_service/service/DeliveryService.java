package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.DeliveryResultProducer;
import top.shop.shop1_service.dto.delivery.DeliveryOrderDto;
import top.shop.shop1_service.entity.DeliveryOrder;
import top.shop.shop1_service.util.DeliveryStatus;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryResultProducer deliveryResultProducer;
    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;

    public void receiveDeliveryFromStorage(DeliveryOrderDto deliveryOrderDto) {

        deliveryOrderDto.setDeliveryStatus(DeliveryStatus.READY_FOR_RECIPIENT);

        mongoTemplate.save(modelMapper.map(deliveryOrderDto, DeliveryOrder.class));
        //TODO here need email notification customer - order delivered
    }

    public DeliveryStatus acceptingDelivery(Long orderNumber, boolean isAccept) {
        DeliveryOrder deliveryOrder = mongoTemplate.findById(orderNumber, DeliveryOrder.class);
        if (deliveryOrder == null) return DeliveryStatus.NOT_FOUND;

        if (!deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.DELIVERED) && isAccept) {
            deliveryOrder.setDeliveryStatus(DeliveryStatus.DELIVERED);
        } else if (!deliveryOrder.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
            deliveryOrder.setDeliveryStatus(DeliveryStatus.REJECTED);
        }
        mongoTemplate.save(deliveryOrder);

        try {
            deliveryResultProducer.sendMessage(modelMapper.map(deliveryOrder, DeliveryOrderDto.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return deliveryOrder.getDeliveryStatus();
    }

    public DeliveryStatus checkDeliveryStatus(Long orderNumber) {
        DeliveryOrder deliveryOrder = mongoTemplate.findById(orderNumber, DeliveryOrder.class);

        if (deliveryOrder == null) return DeliveryStatus.NOT_FOUND;

        return deliveryOrder.getDeliveryStatus();
    }
}
