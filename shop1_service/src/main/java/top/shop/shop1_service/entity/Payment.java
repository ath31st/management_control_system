package top.shop.shop1_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Document("payments")
public class Payment {
   @Id
   private String paymentUuid;
   private BigDecimal totalPrice;
   private LocalDateTime paymentDate;
   private boolean isExecuted;
   private int minutesBeforeExpiration;
}
