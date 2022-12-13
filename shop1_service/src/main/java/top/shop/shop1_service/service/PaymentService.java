package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.PaymentProducer;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.PaymentServiceException;
import top.shop.shop1_service.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ProductPricingService productPricingService;
    private final PaymentProducer paymentProducer;
    private final MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper;
    private static final int MINUTES_BEFORE_EXPIRATION = 5; // after that time order will close like expired

    public Payment createPayment(String productName, int amount) {
        ProductPricing pp = productPricingService.getProductPricing(productName);
        Payment payment = new Payment();

        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentUuid(UUID.randomUUID().toString());
        payment.setPaymentStatus(PaymentStatus.PROCESSING);
        payment.setMinutesBeforeExpiration(MINUTES_BEFORE_EXPIRATION);
        payment.setTotalPrice(BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(pp.getPrice())));

        return mongoTemplate.save(payment);
    }

    public PaymentDto getPaymentDtoFromPayment(Payment payment) {
        return modelMapper.map(payment, PaymentDto.class);
    }

    public Payment getPayment(String paymentUuid) {
        return mongoTemplate.findById(paymentUuid, Payment.class);
    }

    public void receivePaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        checkExistingPayment(paymentRequestDto.getPaymentUuid());

        Payment payment = getPayment(paymentRequestDto.getPaymentUuid());

        checkIsExpiredPayment(payment);
        checkIsRightTotalPrice(payment, paymentRequestDto);

        try {
            paymentProducer.sendMessage(paymentRequestDto);
        } catch (JsonProcessingException e) {
            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        payment.setPaymentStatus(PaymentStatus.EXECUTED);
        mongoTemplate.save(payment);
    }

    private void checkExistingPayment(String paymentUuid) {
        if (!mongoTemplate.exists(Query.query(Criteria.where("paymentUuid").is(paymentUuid)), Payment.class))
            throw new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with " + paymentUuid + " UUID not found!");
    }

    private void checkIsExpiredPayment(Payment payment) {
        if (LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()))) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            mongoTemplate.save(payment);

            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Payment is expired, please try place an order again.");
        }
    }

    private void checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        if (!(payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0)) {
            payment.setPaymentStatus(PaymentStatus.CANCELED);
            mongoTemplate.save(payment);

            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Your total price not equals total price in your order.");
        }
    }

}
