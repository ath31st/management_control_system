package top.shop.gateway.util.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountWrapper {
    private List<DiscountDto> discountList;
    private List<PrivateDiscountDto> privateDiscountList;
    private List<CommonDiscountDto> commonDiscountList;
}
