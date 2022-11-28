package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.CatalogueDto;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.StorageService;
import top.shop.gateway.service.UserService;
import top.shop.gateway.util.wrapper.ProductPricingWrapper;
import top.shop.gateway.util.wrapper.ProductWrapper;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CatalogueController {

    private final CatalogueService catalogueService;
    private final StorageService storageService;
    private final UserService userService;

    @GetMapping("/catalogue")
    public String catalogue(Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        String shopUrl = userService.getUserAttribute("shopUrl");

        model.addAttribute("shopServiceName", shopServiceName);
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage(shopServiceName));
        model.addAttribute("productPricingWrapper", catalogueService.getProductPricingWrapperFromShop(shopUrl));
        return "catalogue-templates/catalogue";
    }

    @PostMapping("/catalogue")
    public String catalogue(@Valid @ModelAttribute("productPricingWrapper") ProductPricingWrapper productPricingWrapper,
                            BindingResult bindingResult,
                            Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        String shopUrl = userService.getUserAttribute("shopUrl");

        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueFromStorage", catalogueService.getCatalogueFromStorage(shopServiceName));
            model.addAttribute("productPricingWrapper", productPricingWrapper);
            return "catalogue-templates/catalogue";
        }

        catalogueService.sendProductPricingWrapperToShop(productPricingWrapper, shopUrl);

        return "redirect:/catalogue";
    }

    @GetMapping("/new-catalogue")
    public String createCatalogue(Model model) {
        model.addAttribute("productWrapper", storageService.getProductWrapper());
        model.addAttribute("catalogueDto", new CatalogueDto());

        return "catalogue-templates/new-catalogue";
    }

    @PostMapping("/new-catalogue")
    public String createCatalogue(@Valid @ModelAttribute("catalogueDto") CatalogueDto catalogueDto,
                                  @RequestParam(value = "productServiceNames", required = false) String[] productServiceNames,
                                  BindingResult bindingResult, Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        ProductWrapper productWrapper = storageService.getProductWrapper();

        if (bindingResult.hasErrors()) {
            model.addAttribute("productWrapper", productWrapper);
            model.addAttribute("catalogueDto", catalogueDto);
            return "catalogue-templates/new-catalogue";
        }
        try {
            catalogueDto = catalogueService.prepareCatalogue(shopServiceName, productServiceNames, productWrapper);
            catalogueService.sendCatalogueToStorage(catalogueDto);
        } catch (HttpClientErrorException e) {
            model.addAttribute("productWrapper", productWrapper);
            model.addAttribute("catalogueDto", catalogueDto);
            return "catalogue-templates/new-catalogue";
        }

        return "redirect:/catalogue";
    }

    @GetMapping("/edit-catalogue/{shopServiceName}")
    public String updateCatalogue(@PathVariable String shopServiceName, Model model) {
        CatalogueDto catalogueDto = catalogueService.getCatalogueFromStorage(shopServiceName);
        ProductWrapper productWrapper = storageService.getProductWrapperWithoutCatalogue(catalogueDto);

        model.addAttribute("productWrapper", productWrapper);
        model.addAttribute("catalogueDto", catalogueDto);
        return "catalogue-templates/edit-catalogue";
    }

    @PostMapping("/edit-catalogue")
    public String updateCatalogue(@Valid @ModelAttribute("catalogueDto") CatalogueDto catalogueDto,
                                  @RequestParam(value = "addProductServiceNames", required = false) String[] addProductServiceNames,
                                  @RequestParam(value = "deleteProductServiceNames", required = false) String[] deleteProductServiceNames,
                                  BindingResult bindingResult, Model model) {
        String shopServiceName = userService.getUserAttribute("shopServiceName");
        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueDto", catalogueDto);
            return "catalogue-templates/edit-catalogue";
        }
        model.addAttribute("catalogueDto", catalogueDto);
        catalogueService.sendCatalogueChangesToStorage(shopServiceName, addProductServiceNames, deleteProductServiceNames);
        return "redirect:/catalogue";
    }

}
