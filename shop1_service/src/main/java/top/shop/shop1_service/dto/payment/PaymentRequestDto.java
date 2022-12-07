package top.shop.shop1_service.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestDto {
    private String paymentUuid;
    private BigDecimal totalPrice;
}
