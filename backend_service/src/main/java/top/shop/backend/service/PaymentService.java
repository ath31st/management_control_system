package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.payment.PaymentRequestDto;
import top.shop.backend.entity.Payment;
import top.shop.backend.exceptionhandler.exception.PaymentServiceException;
import top.shop.backend.repository.PaymentRepository;
import top.shop.backend.service.event.PaymentEvent;
import top.shop.backend.util.PaymentStatus;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Payment getPayment(String paymentUuid) {
        return paymentRepository.findByPaymentUuid(paymentUuid).orElseThrow(
                () -> new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with UUID " + paymentUuid + ", not found!")
        );
    }

    public void receivePaymentRequest(PaymentRequestDto paymentRequestDto) {
        Payment payment = getPayment(paymentRequestDto.getPaymentUuid());

        if(checkIsExpiredPayment(payment)) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
        } else if (checkIsRightTotalPrice(payment, paymentRequestDto)) {
            payment.setPaymentStatus(PaymentStatus.REJECTION);
        } else {
            payment.setPaymentStatus(PaymentStatus.EXECUTED);
        }

        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentEvent(payment));
    }

    // TODO make here chargeback logic when payment is failure

    private boolean checkIsExpiredPayment(Payment payment) {
        return LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()));
    }

    private boolean checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        return !(payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0);
    }
}
