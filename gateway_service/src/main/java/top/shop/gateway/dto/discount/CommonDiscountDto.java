package top.shop.gateway.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommonDiscountDto extends DiscountDto {
    private String promoCode;
    private Long numberOfAvailable;
    private boolean isStacking;
}
