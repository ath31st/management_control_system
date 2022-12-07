package top.shop.shop1_service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {
   private String paymentUuid;
   private double totalPrice;
   private LocalDateTime paymentDate;
   private int minutesBeforeExpiration;
}
