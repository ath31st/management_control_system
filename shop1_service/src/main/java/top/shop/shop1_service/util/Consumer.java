package top.shop.shop1_service.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.DeliveryOrderDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {
    private static final String DELIVERY_TOPIC = "${topic.delivery.name}";

    private final ObjectMapper objectMapper;


    @KafkaListener(topics = DELIVERY_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        DeliveryOrderDto deliveryOrderDto = objectMapper.readValue(message, DeliveryOrderDto.class);
        log.info("delivery order {} for {} has been successfully completed",
                deliveryOrderDto.getProductName(),deliveryOrderDto.getCustomerName());
    }

}