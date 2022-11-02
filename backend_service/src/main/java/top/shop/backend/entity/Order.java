package top.shop.backend.entity;

import lombok.*;

import javax.persistence.*;
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
    private boolean isExecuted;
}
