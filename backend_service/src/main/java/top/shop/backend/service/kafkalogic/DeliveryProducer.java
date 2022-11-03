package top.shop.backend.service.kafkalogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.DeliveryOrderDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryProducer {

    @Value("${topic.delivery.name}")
    private String deliveryTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(DeliveryOrderDto deliveryOrderDto) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(deliveryOrderDto);
        kafkaTemplate.send(deliveryTopic, orderAsMessage);

        log.info("delivery order produced {}", orderAsMessage);

        return "message sent";
    }
}