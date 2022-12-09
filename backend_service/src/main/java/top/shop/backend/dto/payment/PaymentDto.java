package top.shop.backend.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.backend.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {
   private String paymentUuid;
   private BigDecimal totalPrice;
   private LocalDateTime paymentDate;
   private PaymentStatus paymentStatus;
   private int minutesBeforeExpiration;
}
