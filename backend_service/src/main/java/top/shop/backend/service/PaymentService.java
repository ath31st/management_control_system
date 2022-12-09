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
    private final OrderService orderService;

    public Payment getPayment(String paymentUuid) {
        return paymentRepository.findByPaymentUuid(paymentUuid).orElseThrow(
                () -> new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with UUID " + paymentUuid + ", not found!")
        );
    }

    public void receivePaymentRequest(PaymentRequestDto paymentRequestDto) {
        Payment payment = getPayment(paymentRequestDto.getPaymentUuid());
        checkIsExpiredPayment(payment);
        checkIsRightTotalPrice(payment, paymentRequestDto);

        payment.setPaymentStatus(PaymentStatus.EXECUTED);
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentEvent(payment));
    }

    // TODO make here chargeback logic when payment is failure

    private void checkIsExpiredPayment(Payment payment) {
        if (LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()))) {
            payment.setPaymentStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

            //throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Payment is expired, please try place an order again.");
        }
    }

    private void checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        if (!(payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0)) {
            payment.setPaymentStatus(PaymentStatus.REJECTION);
            paymentRepository.save(payment);

            //throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Your total price not equals total price in your order.");
        }
    }
}
