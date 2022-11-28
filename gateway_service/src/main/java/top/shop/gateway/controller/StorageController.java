package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.product.ProductDto;
import top.shop.gateway.service.CategoryService;
import top.shop.gateway.service.StorageService;
import top.shop.gateway.util.wrapper.ProductWrapper;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;
    private final CategoryService categoryService;

    @GetMapping("/storage")
    public String storageHandler(Model model, Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        String accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();

        model.addAttribute("wrapper", storageService.getProductWrapper(accessToken));

        return "storage-templates/storage";
    }

    @PostMapping("/storage")
    public String storageHandler(@Valid ProductWrapper productWrapper,
                                 BindingResult bindingResult,
                                 Model model, Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        String accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();

        if (bindingResult.hasErrors()) {
            model.addAttribute("wrapper", storageService.getProductWrapper(accessToken));
            return "storage-templates/storage";
        }

        storageService.sendProductWrapperToBackend(productWrapper);
        model.addAttribute("message", "Amount updated ");
        model.addAttribute("wrapper", storageService.getProductWrapper(accessToken));

        return "redirect:/storage";
    }

    @GetMapping("new-product")
    public String productHandler(Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categoryList", categoryService.getCategories());
        return "storage-templates/new-product";
    }

    @PostMapping("/new-product")
    public String productHandler(@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productDto", productDto);
            model.addAttribute("categoryList", categoryService.getCategories());
            return "storage-templates/new-product";
        }
        try {
            storageService.createProduct(productDto);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("serviceName", "productDto.serviceName", "Product already exists for this service name.");
            model.addAttribute("productDto", productDto);
            return "storage-templates/new-product";
        }

        return "redirect:/storage";
    }

}
