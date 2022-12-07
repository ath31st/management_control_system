package top.shop.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {
   private String paymentUuid;
   private BigDecimal totalPrice;
   private LocalDateTime paymentDate;
   private boolean isExecuted;
   private int minutesBeforeExpiration;
}
