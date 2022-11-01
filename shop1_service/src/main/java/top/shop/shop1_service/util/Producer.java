package top.shop.shop1_service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.OrderDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {

    @Value("${topic.name}")
    private String orderTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(OrderDto orderDto) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(orderDto);
        kafkaTemplate.send(orderTopic, orderAsMessage);

        log.info("order produced {}", orderAsMessage);

        return "message sent";
    }
}