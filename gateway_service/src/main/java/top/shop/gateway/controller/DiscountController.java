package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.discount.DiscountDto;
import top.shop.gateway.dto.discount.PrivateDiscountDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.DiscountService;
import top.shop.gateway.service.UserService;
import top.shop.gateway.util.wrapper.DiscountWrapper;

import javax.validation.Valid;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
    private final CatalogueService catalogueService;
    private final UserService userService;

    @GetMapping("/discounts")
    public String discounts(Model model) {
        String shopUrl = userService.getUserAttribute("shopUrl");
        DiscountWrapper discountWrapper = discountService.getDiscounts(shopUrl);

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
                                  @RequestParam(value = "productServiceNames", required = false) String[] productServiceNames,
                                  BindingResult bindingResult, Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        String shopUrl = userService.getUserAttribute("shopUrl");

        if (bindingResult.hasErrors()) {
            model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }
        try {
            DiscountWrapper discountWrapper = discountService.prepareDiscountWrapper(productServiceNames, discountDto, shopServiceName);
            discountService.sendDiscountWrapper(discountWrapper, shopUrl);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("productServiceName", "discountDto.productServiceName", Objects.requireNonNull(e.getMessage()));
            model.addAttribute("discountDto", discountDto);
            return "discount-templates/new-discount";
        }

        return "redirect:/discounts";
    }

    @GetMapping("/new-private-discount")
    public String privateDiscountHandler(Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        String shopUrl = userService.getUserAttribute("shopUrl");

        model.addAttribute("customersUsername", discountService.getCustomersUsername(shopUrl));
        model.addAttribute("privateDiscountDto", new PrivateDiscountDto());
        model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
        return "discount-templates/new-private-discount";
    }

    @PostMapping("/new-private-discount")
    public String discountHandler(@Valid @ModelAttribute("privateDiscountDto") PrivateDiscountDto privateDiscountDto,
                                  @RequestParam(value = "productServiceNames", required = false) String[] productServiceNames,
                                  @RequestParam(value = "customers", required = false) String[] customers,
                                  BindingResult bindingResult, Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        String shopUrl = userService.getUserAttribute("shopUrl");

        if (bindingResult.hasErrors()) {
            model.addAttribute("productListFromCatalogue", catalogueService.getCatalogueFromStorage(shopServiceName).getProducts());
            model.addAttribute("privateDiscountDto", privateDiscountDto);
            return "discount-templates/new-private-discount";
        }
        try {
            DiscountWrapper discountWrapper = discountService.prepareDiscountWrapper(productServiceNames, privateDiscountDto, shopServiceName);
            discountService.sendDiscountWrapper(discountWrapper, shopUrl);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("productServiceName", "privateDiscountDto.productServiceName", Objects.requireNonNull(e.getMessage()));
            model.addAttribute("privateDiscountDto", privateDiscountDto);
            return "discount-templates/new-private-discount";
        }

        return "redirect:/discounts";
    }
}
