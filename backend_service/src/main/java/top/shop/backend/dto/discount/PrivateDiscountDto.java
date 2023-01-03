package top.shop.backend.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PrivateDiscountDto extends DiscountDto {
    private String promoCode;
    private boolean isStacking;
    private String customerUsername;
}
