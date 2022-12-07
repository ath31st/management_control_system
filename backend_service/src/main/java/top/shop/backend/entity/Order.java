package top.shop.backend.entity;

import lombok.*;
import top.shop.backend.util.OrderStatus;

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
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    private String productName;
    private String customerName;
    private LocalDateTime orderDate;
    private LocalDateTime executionDate;
    private OrderStatus status;
    private int amount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;
}
