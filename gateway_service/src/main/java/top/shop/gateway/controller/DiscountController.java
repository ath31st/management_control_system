package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.service.DiscountService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/discounts")
    public String discounts(Model model) {
        List<DiscountDto> discounts = discountService.getDiscounts();
        List<PrivateDiscountDto> privateDiscounts = discountService.getPrivateDiscounts();
        List<CommonDiscountDto> commonDiscounts = discountService.getCommonDiscounts();
        model.addAttribute("discounts", discounts);
        model.addAttribute("privateDiscounts", privateDiscounts);
        model.addAttribute("commonDiscounts", commonDiscounts);
        return "discount-templates/discounts";
    }
}
