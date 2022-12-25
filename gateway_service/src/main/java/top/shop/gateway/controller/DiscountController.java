package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.discount.CommonDiscountDto;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.DiscountService;
import top.shop.gateway.service.UserService;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final CatalogueService catalogueService;
    private final UserService userService;

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

    @GetMapping("/new-discount")
    public String discountHandler(Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");

        model.addAttribute("shopServiceName", shopServiceName);
        model.addAttribute("discountDto", new DiscountDto());
        model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
        return "discount-templates/new-discount";
    }

    @PostMapping("/new-discount")
    public String discountHandler(@Valid @ModelAttribute("discountDto") DiscountDto discountDto,
                                  BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }
        try {
            discountService.createDiscount(discountDto);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("productServiceName", "discountDto.productServiceName", "Discount already exists for this product service name.");
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }

        return "redirect:/discounts";
    }
}
