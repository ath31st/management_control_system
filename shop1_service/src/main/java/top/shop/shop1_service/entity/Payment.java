package top.shop.shop1_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.shop.shop1_service.entity.discount.CommonDiscount;
import top.shop.shop1_service.entity.discount.PrivateDiscount;
import top.shop.shop1_service.util.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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
   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "deliveryOrder_id")
   private DeliveryOrder deliveryOrder;
   @OneToOne
   @JoinColumn(name = "commonDiscount_id")
   private CommonDiscount commonDiscount;
   @OneToOne
   @JoinColumn(name = "privateDiscount_id")
   private PrivateDiscount privateDiscount;
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "customer_id", nullable = false)
   @ToString.Exclude
   private Customer customer;

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Payment payment = (Payment) o;
      return Objects.equals(id, payment.id) && Objects.equals(paymentUuid, payment.paymentUuid);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, paymentUuid);
   }

   @Override
   public String toString() {
      return "Payment{" +
              "id=" + id +
              ", paymentUuid='" + paymentUuid + '\'' +
              ", totalPrice=" + totalPrice +
              ", paymentDate=" + paymentDate +
              ", paymentStatus=" + paymentStatus +
              ", minutesBeforeExpiration=" + minutesBeforeExpiration +
              '}';
   }
}
