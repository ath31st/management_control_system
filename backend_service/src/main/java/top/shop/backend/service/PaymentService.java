package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.payment.PaymentRequestDto;
import top.shop.backend.entity.Payment;
import top.shop.backend.exceptionhandler.exception.PaymentServiceException;
import top.shop.backend.repository.PaymentRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment getPayment(String paymentUuid) {
        return paymentRepository.findByPaymentUuid(paymentUuid).orElseThrow(
                () -> new PaymentServiceException(HttpStatus.NOT_FOUND, "Payment with UUID " + paymentUuid + ", not found!")
        );
    }

    public void receivePaymentRequest(PaymentRequestDto paymentRequestDto) {
        Payment payment = getPayment(paymentRequestDto.getPaymentUuid());
        checkIsExpiredPayment(payment);
        checkIsRightTotalPrice(payment, paymentRequestDto);
        ...
    }

    private boolean checkIsExpiredPayment(Payment payment) {
        return Duration.between(LocalDateTime.now(), payment.getPaymentDate()).toMinutes() <
                payment.getMinutesBeforeExpiration();
    }

    private boolean checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        return payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0;
    }
}
