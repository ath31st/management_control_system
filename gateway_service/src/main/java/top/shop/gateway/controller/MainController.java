package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.ShopDto;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.ShopService;
import top.shop.gateway.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final CatalogueService catalogueService;
    private final ShopService shopService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "Hello!");
        return "index";
    }

    @GetMapping("/catalogue")
    public String catalogue(Model model, Principal principal) {
        UserDto user = userService.getUserDto(principal.getName());

        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
        model.addAttribute("catalogueFromShop", catalogueService.getCatalogueFromShop(user.getShopUrl()));
        return "catalogue";
    }

    @PostMapping("/catalogue")
    public String catalogue(@Valid @ModelAttribute("catalogueFromShop") CatalogueDto catalogueDto,
                            BindingResult bindingResult,
                            Model model,
                            Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
            model.addAttribute("catalogueFromShop", catalogueDto);
            return "catalogue";
        }

        UserDto user = userService.getUserDto(principal.getName());
        model.addAttribute("message", "Prices updated ");
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
        catalogueService.sendPricesToShop(catalogueDto, user.getShopUrl());
        return "catalogue";
    }

    @GetMapping("/shops")
    public String shops(Model model) {
        List<ShopDto> shops = shopService.getShops();
        model.addAttribute("totalBalance", shopService.getTotalBalance(shops));
        model.addAttribute(shops);
        return "shops";
    }

    @GetMapping("/new-shop")
    public String shopHandler(Model model) {
        model.addAttribute("shopData", new ShopDto());
        return "new-shop";
    }

    @PostMapping("/new-shop")
    public String shopHandler(@Valid @ModelAttribute("shopData") ShopDto shopData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("shopData", shopData);
            return "new-shop";
        }
        try {
            shopService.createShop(shopData);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("name", "shopData.name", "Shop already exists for this name.");
            model.addAttribute("shopData", shopData);
            return "new-shop";
        }

        return "redirect:/shops";
    }

}
