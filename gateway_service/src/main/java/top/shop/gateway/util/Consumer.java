package top.shop.gateway.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.gateway.dto.OrderDto;
import top.shop.gateway.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumer {
   // private static final String orderTopic = "${topic.name}";
    private static final String orderTopic = "t.order";

    private final ObjectMapper objectMapper;
    private final OrderService orderService;


    @KafkaListener(topics = orderTopic)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        OrderDto orderDto = objectMapper.readValue(message, OrderDto.class);
        orderService.persistOrder(orderDto);
    }

}