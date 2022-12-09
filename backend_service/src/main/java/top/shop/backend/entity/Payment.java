package top.shop.backend.entity;

import lombok.*;
import top.shop.backend.util.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
