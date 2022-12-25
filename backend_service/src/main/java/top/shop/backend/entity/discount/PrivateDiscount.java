package top.shop.backend.entity.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class PrivateDiscount extends Discount {
    private String promoCode;
    private boolean isStacking;
}
