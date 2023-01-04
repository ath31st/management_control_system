package top.shop.gateway.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class PrivateDiscountDto extends DiscountDto {
    @NotEmpty(message = "Promo code string cannot be empty.")
    @NotBlank(message = "Promo code string cannot be blank")
    @Size(min = 3, max = 20, message = "Promo code string must be minimum 3 and maximum 20 characters.")
    private String promoCode;
    private boolean isStacking;
    private String customerEmail;
}
