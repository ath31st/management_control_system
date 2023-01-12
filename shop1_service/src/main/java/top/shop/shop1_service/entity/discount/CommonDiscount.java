package top.shop.shop1_service.entity.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.shop.shop1_service.entity.Payment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@DiscriminatorValue("2")
public class CommonDiscount extends Discount {
    private String promoCode;
    private boolean isStacking;
    private Long numberOfAvailable;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
