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
public class MainController {

    private final UserService userService;
    private final CatalogueService catalogueService;

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

}
