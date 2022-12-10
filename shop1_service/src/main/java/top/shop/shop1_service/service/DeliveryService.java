package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.DeliveryOrderDto;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;

    public void receiveDeliveryFromStorage(DeliveryOrderDto deliveryOrderDto) {

    }
}
