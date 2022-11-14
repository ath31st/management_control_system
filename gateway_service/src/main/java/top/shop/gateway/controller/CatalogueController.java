package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.UserService;
import top.shop.gateway.util.wrapper.ProductPricingWrapper;

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

        model.addAttribute("shopServiceName", user.getShopServiceName());
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage(user.getShopServiceName()));
        model.addAttribute("wrapper", catalogueService.getProductPricingWrapperFromShop(user.getShopUrl()));
        return "catalogue-templates/catalogue";
    }

    @PostMapping("/catalogue")
    public String catalogue(@Valid @ModelAttribute("wrapper") ProductPricingWrapper wrapper,
                            BindingResult bindingResult,
                            Model model,
                            Principal principal) {
        UserDto user = userService.getUserDto(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage(user.getShopServiceName()));
            model.addAttribute("wrapper", wrapper);
            return "catalogue-templates/catalogue";
        }

        catalogueService.sendProductPricingWrapperToShop(wrapper, user.getShopUrl());
        model.addAttribute("message", "Prices updated ");
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage(user.getShopServiceName()));

        return "catalogue-templates/catalogue";
    }

    @GetMapping("/new-catalogue")
    public String createCatalogue(Model model, Principal principal) {
        UserDto user = userService.getUserDto(principal.getName());

        model.addAttribute("catalogueDto", new CatalogueDto());
        model.addAttribute("shopServiceName", user.getShopServiceName());
        return "catalogue-templates/new-catalogue";
    }

    @GetMapping("/edit-catalogue/{shopServiceName}")
    public String updateCatalogue(@PathVariable String shopServiceName, Model model) {
        CatalogueDto catalogueDto = catalogueService.getCatalogueFromStorage(shopServiceName);
        model.addAttribute("catalogueDto", catalogueDto);
        return "catalogue-templates/edit-catalogue";
    }

    @PostMapping("/edit-catalogue/")
    public String updateCatalogue(@Valid @ModelAttribute("catalogueDto") CatalogueDto catalogueDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueDto", catalogueDto);
            return "catalogue-templates/edit-catalogue";
        }
        model.addAttribute("catalogueDto", catalogueDto);

        catalogueService.sendCatalogueToStorage(catalogueDto);
        return "redirect:/catalogue";
    }

}
