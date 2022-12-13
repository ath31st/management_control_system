package top.shop.shop1_service.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.delivery.DeliveryResultDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryResultProducer {

    @Value("${topic.delivery-result.name}")
    private String deliveryResultTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(DeliveryResultDto dto) throws JsonProcessingException {
        String deliveryResultAsMessage = objectMapper.writeValueAsString(dto);
        kafkaTemplate.send(deliveryResultTopic, deliveryResultAsMessage);

        log.info("delivery result produced {}", deliveryResultAsMessage);

        return "message sent";
    }
}