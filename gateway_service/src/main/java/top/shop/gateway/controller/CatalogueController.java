package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final UserService userService;

    @GetMapping("/catalogue")
    public String catalogue(Model model, Principal principal) {
        UserDto user = userService.getUserDto(principal.getName());

        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
        model.addAttribute("catalogueFromShop", catalogueService.getProductPricingListFromShop(user.getShopUrl()));
        return "catalogue-templates/catalogue";
    }

    @PostMapping("/catalogue")
    public String catalogue(@Valid @ModelAttribute("catalogueFromShop") CatalogueDto catalogueDto,
                            BindingResult bindingResult,
                            Model model,
                            Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
            model.addAttribute("catalogueFromShop", catalogueDto);
            return "catalogue-templates/catalogue";
        }

        UserDto user = userService.getUserDto(principal.getName());
        model.addAttribute("message", "Prices updated ");
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage());
        catalogueService.sendProductPricingListToShop(catalogueDto, user.getShopUrl());
        return "catalogue-templates/catalogue";
    }

}
