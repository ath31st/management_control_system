package top.shop.shop1_service.entity.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
}
