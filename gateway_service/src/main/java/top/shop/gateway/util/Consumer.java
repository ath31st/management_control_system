package top.shop.gateway.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.gateway.service.OrderService;

//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class Consumer {
//    private static final String ORDER_TOPIC = "${topic.name}";
//
//    private final ObjectMapper objectMapper;
//    private final OrderService orderService;
//
//
//    @KafkaListener(topics = ORDER_TOPIC)
//    public void consumeMessage(String message) throws JsonProcessingException {
//        log.info("message consumed {}", message);
//
//    }
//
//}