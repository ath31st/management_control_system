package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.PaymentDto;
import top.shop.backend.entity.Order;
import top.shop.backend.entity.Payment;
import top.shop.backend.repository.PaymentRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public Payment savePayment(PaymentDto paymentDto, Order order) {
        Payment p = modelMapper.map(paymentDto, Payment.class);
        p.setOrder(order);
        return paymentRepository.save(p);
    }
}
