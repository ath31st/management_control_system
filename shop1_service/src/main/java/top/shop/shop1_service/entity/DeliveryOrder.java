package top.shop.shop1_service.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.shop.shop1_service.util.DeliveryStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class DeliveryOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String orderUuidNumber;
    private String shopServiceName;
    private String shopName;
    private int amount;
    private double totalPrice;
    private DeliveryStatus deliveryStatus;
    @ManyToMany
    @JoinTable(
            name = "product_deliveryOrder",
            joinColumns = @JoinColumn(name = "deliveryOrder_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @ToString.Exclude
    private List<Product> products;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryOrder that = (DeliveryOrder) o;
        return Objects.equals(id, that.id) && Objects.equals(orderUuidNumber, that.orderUuidNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderUuidNumber);
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id=" + id +
                ", orderUuidNumber='" + orderUuidNumber + '\'' +
                ", shopServiceName='" + shopServiceName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", amount=" + amount +
                ", totalPrice=" + totalPrice +
                ", deliveryStatus=" + deliveryStatus +
                '}';
    }
}
