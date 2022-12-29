package top.shop.shop1_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.shop1_service.util.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;
   private String paymentUuid;
   private BigDecimal totalPrice;
   private LocalDateTime paymentDate;
   private PaymentStatus paymentStatus;
   private int minutesBeforeExpiration;

}
