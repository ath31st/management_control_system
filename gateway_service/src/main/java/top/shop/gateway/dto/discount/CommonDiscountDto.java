package top.shop.gateway.dto.discount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class CommonDiscountDto extends DiscountDto {
    @NotEmpty(message = "Promo code string cannot be empty.")
    @NotBlank(message = "Promo code string cannot be blank")
    @Size(min = 3, max = 20, message = "Promo code string must be minimum 3 and maximum 20 characters.")
    private String promoCode;
    @Min(value = 1, message = "Number cannot be lower 1")
    @Max(value = 100000, message = "Number cannot be higher 100 000")
    private Long numberOfAvailable;
    private boolean isStacking;
}
