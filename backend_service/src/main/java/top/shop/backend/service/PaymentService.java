package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import top.shop.backend.dto.payment.PaymentRequestDto;
import top.shop.backend.entity.Order;
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


    }

    private void checkIsExpiredPayment(Payment payment) {
        if (LocalDateTime.now().isAfter(payment.getPaymentDate().plusMinutes(payment.getMinutesBeforeExpiration()))) {
            Order order = orderService.
           // throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Payment is expired, please try place an order again.");
        }
    }

    private void checkIsRightTotalPrice(Payment payment, PaymentRequestDto paymentRequestDto) {
        if (!(payment.getTotalPrice().compareTo(paymentRequestDto.getTotalPrice()) == 0))
            throw new PaymentServiceException(HttpStatus.BAD_REQUEST, "Your total price not equals total price in your order.");
    }
}
