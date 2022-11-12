package top.shop.shop1_service.util.wrapper;

import lombok.*;
import top.shop.shop1_service.dto.ProductPricingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPricingWrapper {
    private List<ProductPricingDto> pricingDtoList;
}
