package top.shop.shop1_service.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductAmountDto;
import top.shop.shop1_service.entity.Catalogue;
import top.shop.shop1_service.service.CatalogueService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductAmountConsumer {

    private static final String AMOUNT_TOPIC = "${topic.amount.name}";

    private final ObjectMapper objectMapper;
    private final CatalogueService catalogueService;

    @KafkaListener(topics = AMOUNT_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        ProductAmountDto productAmountDto = objectMapper.readValue(message, ProductAmountDto.class);
        catalogueService.updateAmountProductInCatalogue(productAmountDto);

        log.info("product amount {} has been successfully received", productAmountDto.getOnDate());
    }

}