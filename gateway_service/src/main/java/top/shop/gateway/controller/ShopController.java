package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.ShopDto;
import top.shop.gateway.service.ShopService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/shops")
    public String shops(Model model) {
        List<ShopDto> shops = shopService.getShops();
        model.addAttribute("totalBalance", shopService.getTotalBalance(shops));
        model.addAttribute("shopDtoList", shops);
        return "shop-templates/shops";
    }

    @GetMapping("/edit-shop/{shopServiceName}")
    public String shopChangesHandler(@PathVariable String shopServiceName, Model model) {
        ShopDto shopDto = shopService.getShop(shopServiceName);
        model.addAttribute("shopDto", shopDto);
        return "shop-templates/edit-shop";
    }

    @PostMapping("/edit-shop")
    public String shopChangesHandler(@Valid @ModelAttribute("shopDto") ShopDto shopDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("shopDto", shopDto);
            return "shop-templates/edit-shop";
        }
        model.addAttribute("shopDto", shopDto);

        shopService.sendShopChanges(shopDto);
        return "redirect:/shops";
    }

    @GetMapping("/new-shop")
    public String shopHandler(Model model) {
        model.addAttribute("shopData", new ShopDto());
        return "shop-templates/new-shop";
    }

    @PostMapping("/new-shop")
    public String shopHandler(@Valid @ModelAttribute("shopData") ShopDto shopData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("shopData", shopData);
            return "shop-templates/new-shop";
        }
        try {
            shopService.createShop(shopData);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("serviceName", "shopData.serviceName", "Shop already exists for this service name.");
            model.addAttribute("shopData", shopData);
            return "shop-templates/new-shop";
        }

        return "redirect:/shops";
    }

}
