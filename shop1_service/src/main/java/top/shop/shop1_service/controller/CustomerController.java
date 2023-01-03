package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.OrderDto;
import top.shop.shop1_service.dto.payment.PaymentDto;
import top.shop.shop1_service.dto.payment.PaymentRequestDto;
import top.shop.shop1_service.service.DeliveryService;
import top.shop.shop1_service.service.OrderService;
import top.shop.shop1_service.service.PaymentService;
import top.shop.shop1_service.util.DeliveryStatus;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;

    @PostMapping("/order")
    public ResponseEntity<PaymentDto> orderHandler(@Valid @RequestBody OrderDto orderDto) {
        log.info("order request received");
        return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    @GetMapping("/delivery/status/{orderUuidNumber}")
    public ResponseEntity<Map<String, Enum>> checkDeliveryOrderStatus(@PathVariable String orderUuidNumber) {
        paymentService.checkPaymentStatus(orderUuidNumber);
        deliveryService.checkDeliveryStatus(orderUuidNumber);
        Map<String, Enum> response = Map.of(
                "Payment status", paymentService.checkPaymentStatus(orderUuidNumber),
                "Delivery status", deliveryService.checkDeliveryStatus(orderUuidNumber));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/delivery/accepting/{orderUuidNumber}")
    public ResponseEntity<DeliveryStatus> acceptingDeliveryOrder(@PathVariable String orderUuidNumber, @RequestParam boolean isAccept) {
        return ResponseEntity.ok(deliveryService.acceptingDelivery(orderUuidNumber, isAccept));
    }

    @PostMapping("/payment")
    public String orderHandler(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        log.info("payment request received");
        paymentService.receivePaymentRequestDto(paymentRequestDto);
        return "Payment with UUID " + paymentRequestDto.getPaymentUuid() + " accepted";
    }

}
