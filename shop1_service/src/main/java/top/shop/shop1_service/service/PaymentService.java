package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.PaymentProducer;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;
import top.shop.shop1_service.entity.ProductPricing;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ProductPricingService productPricingService;
    private final PaymentProducer paymentProducer;
    private static final int MINUTES_BEFORE_EXPIRATION = 5; // after that time order will close like expired

    public PaymentDto createPaymentDto(String productName, int amount) {
        ProductPricing pp = productPricingService.getProductPricing(productName);
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setPaymentDate(LocalDateTime.now());
        paymentDto.setPaymentUuid(UUID.randomUUID().toString());
        paymentDto.setExecuted(false);
        paymentDto.setMinutesBeforeExpiration(MINUTES_BEFORE_EXPIRATION);
        paymentDto.setTotalPrice(BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(pp.getPrice())));

        return paymentDto;
    }

    public void receivePaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        paymentProducer.sendMessage();
    }


}
