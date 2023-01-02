package top.shop.shop1_service.entity.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "common_discounts")
public class CommonDiscount extends Discount {
    private String promoCode;
    private boolean isStacking;
    private Long numberOfAvailable;
}
