package top.shop.backend.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.product.ProductAmountDto;
import top.shop.backend.service.event.ProductAmountEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAmountProducer {

    @Value("${topic.amount.name}")
    private String productAmountTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @EventListener
    public void sendMessage(ProductAmountEvent event) throws JsonProcessingException {
        ProductAmountDto productAmountDto = (ProductAmountDto) event.getSource();
        String productAmountAsMessage = objectMapper.writeValueAsString(productAmountDto);
        kafkaTemplate.send(productAmountTopic, productAmountAsMessage);

        log.info("product amount produced {}", productAmountAsMessage);

    }
}