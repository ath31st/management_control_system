package top.shop.gateway.dto.discount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommonDiscountDto extends PrivateDiscountDto {
    private Long numberOfAvailable;
}
