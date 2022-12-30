package top.shop.shop1_service.entity;

import lombok.*;
import top.shop.shop1_service.entity.discount.PrivateDiscount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String username;
    private BigDecimal balance;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<PrivateDiscount> privateDiscounts;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<DeliveryOrder> deliveryOrders;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Payment> payments;
}
