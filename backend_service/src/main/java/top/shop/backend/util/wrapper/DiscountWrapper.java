package top.shop.backend.util.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.backend.dto.discount.CommonDiscountDto;
import top.shop.backend.dto.discount.DiscountDto;
import top.shop.backend.dto.discount.PrivateDiscountDto;

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
