package top.shop.backend.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.backend.util.wrapper.CategoryWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryWrapperProducer {

    @Value("${topic.category.name}")
    private String categoryWrapperTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(CategoryWrapper categoryWrapper) throws JsonProcessingException {
        String categoryWrapperAsMessage = objectMapper.writeValueAsString(categoryWrapper);
        kafkaTemplate.send(categoryWrapperTopic, categoryWrapperAsMessage);

        log.info("category wrapper produced {}", categoryWrapperAsMessage);

    }
}