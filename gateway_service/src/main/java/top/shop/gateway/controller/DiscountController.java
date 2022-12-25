package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.DiscountService;
import top.shop.gateway.service.UserService;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final CatalogueService catalogueService;
    private final UserService userService;

    @GetMapping("/discounts")
    public String discounts(Model model) {
        DiscountWrapper discountWrapper = discountService.getDiscounts();

        model.addAttribute("discounts", discountWrapper.getDiscountList());
        model.addAttribute("privateDiscounts", discountWrapper.getPrivateDiscountList());
        model.addAttribute("commonDiscounts", discountWrapper.getCommonDiscountList());
        return "discount-templates/discounts";
    }

    @GetMapping("/new-discount")
    public String discountHandler(Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");

        model.addAttribute("discountDto", new DiscountDto());
        model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
        return "discount-templates/new-discount";
    }

    @PostMapping("/new-discount")
    public String discountHandler(@Valid @ModelAttribute("discountDto") DiscountDto discountDto,
                                  BindingResult bindingResult, Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");

        if (bindingResult.hasErrors()) {
            model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }
        try {
            discountDto.setShopServiceName(shopServiceName);
            discountService.createDiscount(discountDto);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("productServiceName", "discountDto.productServiceName", "Discount already exists for this product service name.");
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }

        return "redirect:/discounts";
    }
}
