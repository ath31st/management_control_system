package top.shop.shop1_service.config.kafkaconfig;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.shop1_service.service.CategoryService;
import top.shop.shop1_service.service.ProductService;
import top.shop.shop1_service.util.wrapper.CategoryWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryWrapperConsumer {
    private static final String CATEGORY_WRAPPER_TOPIC = "${topic.category.name}";

    private final ObjectMapper objectMapper;
    private final CategoryService categoryService;
    private final ProductService productService;

    @KafkaListener(topics = CATEGORY_WRAPPER_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        CategoryWrapper categoryWrapper = objectMapper.readValue(message, CategoryWrapper.class);

        if (categoryWrapper.getDeletedCategory() != null) {
            if (categoryService.existsCategory(categoryWrapper.getReplacementCategory().getServiceName())) {
                categoryService.updateCategory(categoryWrapper.getReplacementCategory());
            } else {
                categoryService.categoryDtoToCategoryConverter(categoryWrapper.getReplacementCategory());
            }
            productService.changeProductsCategoryToDefault(categoryWrapper.getDeletedCategory());
            categoryService.deleteCategory(categoryWrapper.getDeletedCategory());
        }

        if (categoryWrapper.getUpdatedCategory() != null && categoryService.existsCategory(categoryWrapper.getUpdatedCategory().getServiceName())) {
            categoryService.updateCategory(categoryWrapper.getUpdatedCategory());
        }
    }

}