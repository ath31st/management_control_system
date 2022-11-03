package top.shop.backend.service.kafkalogic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.OrderDto;
import top.shop.backend.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private static final String ORDER_TOPIC = "${topic.order.name}";

    private final ObjectMapper objectMapper;
    private final OrderService orderService;


    @KafkaListener(topics = ORDER_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);
        orderService.persistOrder(orderDto);
    }

}