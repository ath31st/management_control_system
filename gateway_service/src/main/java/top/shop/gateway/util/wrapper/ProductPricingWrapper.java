package top.shop.gateway.util.wrapper;

import lombok.*;
import top.shop.gateway.dto.ProductPricingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPricingWrapper {
    private List<ProductPricingDto> pricingDtoList;
}
