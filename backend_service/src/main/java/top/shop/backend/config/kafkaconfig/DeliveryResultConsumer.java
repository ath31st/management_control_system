package top.shop.backend.config.kafkaconfig;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.dto.delivery.DeliveryOrderDto;
import top.shop.backend.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryResultConsumer {
    private static final String DELIVERY_RESULT_TOPIC = "${topic.delivery-result.name}";
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = DELIVERY_RESULT_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        DeliveryOrderDto deliveryOrderDto = objectMapper.readValue(message, DeliveryOrderDto.class);
        orderService.processingDeliveryResult(deliveryOrderDto);
    }

}