package top.shop.shop1_service.entity.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.shop.shop1_service.entity.Customer;
import top.shop.shop1_service.entity.Payment;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@DiscriminatorValue("1")
public class PrivateDiscount extends Discount {
    private String promoCode;
    private boolean isStacking;
    private boolean isApplied;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
