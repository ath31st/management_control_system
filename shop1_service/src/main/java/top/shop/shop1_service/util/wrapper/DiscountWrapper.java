package top.shop.shop1_service.util.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.shop1_service.dto.discount.CommonDiscountDto;
import top.shop.shop1_service.dto.discount.DiscountDto;
import top.shop.shop1_service.dto.discount.PrivateDiscountDto;

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
