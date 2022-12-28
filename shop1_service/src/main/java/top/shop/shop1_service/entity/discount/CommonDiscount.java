package top.shop.shop1_service.entity.discount;

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
public class CommonDiscount extends PrivateDiscount {
    private Long numberOfAvailable;
}
