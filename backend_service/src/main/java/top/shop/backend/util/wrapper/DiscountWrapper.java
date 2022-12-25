package top.shop.backend.util.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
