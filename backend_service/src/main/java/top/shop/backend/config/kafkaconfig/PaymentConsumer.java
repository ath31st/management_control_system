package top.shop.backend.config.kafkaconfig;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.payment.PaymentRequestDto;
import top.shop.backend.entity.Payment;
import top.shop.backend.service.OrderService;
import top.shop.backend.service.PaymentService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private static final String PAYMENT_TOPIC = "${topic.payment.name}";

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    private final OrderService orderService;


    @KafkaListener(topics = PAYMENT_TOPIC)
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("message consumed {}", message);

        PaymentRequestDto paymentRequestDto = objectMapper.readValue(message, PaymentRequestDto.class);
        Payment payment = paymentService.receivePaymentRequest(paymentRequestDto);
        orderService.processingOrder(payment);
    }

}