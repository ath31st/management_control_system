package top.shop.backend.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.CatalogueDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogueProducer {
//
//    @Value("${topic.catalogue.name}")
//    private String catalogueTopic;
//
//    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessage(CatalogueDto catalogueDto) throws JsonProcessingException {
//        String catalogueAsMessage = objectMapper.writeValueAsString(catalogueDto);
//        kafkaTemplate.send(catalogueTopic, catalogueAsMessage);
//
//        log.info("catalogue produced {}", catalogueAsMessage);
//
//    }
}