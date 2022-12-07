package top.shop.shop1_service.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    @Value("${topic.payment.name}")
    private String paymentTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(PaymentRequestDto paymentRequestDto) throws JsonProcessingException {
        String paymentAsMessage = objectMapper.writeValueAsString(paymentRequestDto);
        kafkaTemplate.send(paymentTopic, paymentAsMessage);

        log.info("payment produced {}", paymentAsMessage);

        return "message sent";
    }
}