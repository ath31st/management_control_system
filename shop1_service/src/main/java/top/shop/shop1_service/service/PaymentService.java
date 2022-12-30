package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.PaymentProducer;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;
import top.shop.shop1_service.entity.Customer;
import top.shop.shop1_service.entity.DeliveryOrder;
import top.shop.shop1_service.entity.Payment;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.PaymentServiceException;
import top.shop.shop1_service.repository.PaymentRepository;
import top.shop.shop1_service.service.event.PaymentEvent;
import top.shop.shop1_service.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductPricingService productPricingService;
    private final PaymentProducer paymentProducer;
    private final ApplicationEventPublisher eventPublisher;
    private static final int MINUTES_BEFORE_EXPIRATION = 5; // after that time order will close like expired

    public Payment createPayment(Customer customer, String productName, int amount) {
        ProductPricing pp = productPricingService.getProductPricing(productName);
        Payment payment = new Payment();

        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentUuid(UUID.randomUUID().toString());
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        payment.setMinutesBeforeExpiration(MINUTES_BEFORE_EXPIRATION);
        payment.setTotalPrice(BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(pp.getPrice())));
        payment.setCustomer(customer);

        eventPublisher.publishEvent(new PaymentEvent(payment));

        return paymentRepository.save(payment);
    }

    public PaymentDto getPaymentDtoFromPayment(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentUuid(p.getPaymentUuid());
        dto.setTotalPrice(p.getTotalPrice());
        dto.setPaymentDate(p.getPaymentDate());
        dto.setMinutesBeforeExpiration(p.getMinutesBeforeExpiration());
        dto.setPaymentStatus(p.getPaymentStatus());
        return dto;
    }

    public Payment getPayment(String paymentUuid) {
        return paymentRepository.findByPaymentUuid(paymentUuid).orElseThrow(
                () -> new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with UUID: " + paymentUuid + " not found!")
        );
    }

    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status);
    }

    public void receivePaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        checkExistingPayment(paymentRequestDto.getPaymentUuid());

        Payment payment = getPayment(paymentRequestDto.getPaymentUuid());

        checkPaymentStatus(payment);
        checkIsExpiredPayment(payment);
        checkIsRightTotalPrice(payment, paymentRequestDto);

        try {
            paymentProducer.sendMessage(paymentRequestDto);
        } catch (JsonProcessingException e) {
            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        payment.setPaymentStatus(PaymentStatus.EXECUTED);
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentEvent(payment));
    }

    public PaymentStatus checkPaymentStatus(String paymentUuid) {
        if (paymentRepository.findByPaymentUuid(paymentUuid).isPresent()) {
            return getPayment(paymentUuid).getPaymentStatus();
        } else {
            return PaymentStatus.NOT_FOUND;
        }
    }

    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);
    }

    public void cancelPayment(String paymentUuid) {
        Payment payment = getPayment(paymentUuid);
        payment.setPaymentStatus(PaymentStatus.CANCELED);

        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentEvent(payment));
    }

    public void setDeliveryOrderInPayment(DeliveryOrder order) {
        Payment p = getPayment(order.getOrderUuidNumber());
        p.setDeliveryOrder(order);

        paymentRepository.save(p);
    }

    private void checkExistingPayment(String paymentUuid) {
        if (!paymentRepository.existsByPaymentUuid(paymentUuid))
            throw new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with " + paymentUuid + " UUID not found!");
    }

    private void checkPaymentStatus(Payment payment) {
        if (!payment.getPaymentStatus().equals(PaymentStatus.UNPAID)) {
            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Payment with " + payment.getPaymentUuid() +
                    " UUID has status: " + payment.getPaymentStatus());
        }
    }

    private void checkIsExpiredPayment(Payment payment) {
        if (LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()))) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

            eventPublisher.publishEvent(new PaymentEvent(payment));

            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Payment is expired, please try place an order again.");
        }
    }

    private void checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        if (!(payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0)) {
            payment.setPaymentStatus(PaymentStatus.CANCELED);
            paymentRepository.save(payment);

            eventPublisher.publishEvent(new PaymentEvent(payment));

            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Your total price not equals total price in your order.");
        }
    }

}
