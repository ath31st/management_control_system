package top.shop.shop1_service.config.kafkaconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.exceptionhandler.exception.OrderServiceException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProducer {

    @Value("${topic.order.name}")
    private String orderTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendMessage(OrderDto orderDto) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(orderDto);
        kafkaTemplate.send(orderTopic, orderAsMessage);

        log.info("order produced {}", orderAsMessage);

        return "message sent";
    }

    public void sendMessageWithCallback(OrderDto orderDto) throws JsonProcessingException {
        String orderAsMessage = objectMapper.writeValueAsString(orderDto);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(orderTopic, orderAsMessage);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                throw new OrderServiceException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[{}] with offset=[{}]", orderAsMessage, result.getRecordMetadata().offset());
            }
        });
    }

}