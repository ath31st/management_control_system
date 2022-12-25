package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.service.DiscountService;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/discounts")
    public String discounts(Model model) {
        DiscountWrapper discountWrapper = discountService.getDiscounts();
        List<DiscountDto> discounts = discountWrapper.getDiscountList();
        List<PrivateDiscountDto> privateDiscounts = discountWrapper.getPrivateDiscountList();
        List<CommonDiscountDto> commonDiscounts = discountWrapper.getCommonDiscountList();
        model.addAttribute("discounts", discounts);
        model.addAttribute("privateDiscounts", privateDiscounts);
        model.addAttribute("commonDiscounts", commonDiscounts);
        return "discount-templates/discounts";
    }
}
