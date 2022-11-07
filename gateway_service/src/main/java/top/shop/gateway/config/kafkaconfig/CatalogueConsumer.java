package top.shop.gateway.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.service.CatalogueService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogueConsumer {
    private static final String CATALOGUE_TOPIC = "${topic.catalogue.name}";

    private final CatalogueService catalogueService;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = CATALOGUE_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        CatalogueDto catalogueDto = objectMapper.readValue(message, CatalogueDto.class);
        log.info("catalogue {} has been successfully received", catalogueDto.getCatalogueOnDate());

        catalogueService.setCatalogueFromStorage(catalogueDto);
    }

}