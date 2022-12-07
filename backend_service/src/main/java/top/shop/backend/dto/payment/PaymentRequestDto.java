package top.shop.backend.dto.payment;

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
