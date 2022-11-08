package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.*;
import top.shop.gateway.service.CatalogueService;
import top.shop.gateway.service.ShopService;
import top.shop.gateway.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userData", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String userRegistration(@Valid @ModelAttribute("userData") UserDto userData, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userData", userData);
            return "register";
        }
        try {
            userService.registerUser(userData);
        } catch (ResponseStatusException e) {
            bindingResult.rejectValue("username", "userData.username", "An account already exists for this username.");
            model.addAttribute("userData", userData);
            return "register";
        }
        return "redirect:/index";
    }

    @GetMapping("/catalogue")
    public String catalogue(Model model) {
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogue());
        model.addAttribute("catalogueFromShop", catalogueService.getCatalogueFromShop());
        return "catalogue";
    }

    @PostMapping("/catalogue")
    public String catalogue(@Valid @ModelAttribute("catalogueFromShop") CatalogueDto catalogueDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("catalogueFromStorage", catalogueService.getCatalogue());
            model.addAttribute("catalogueFromShop", catalogueDto);
            return "catalogue";
        }
        model.addAttribute("message", "Prices updated ");
        model.addAttribute("catalogueFromStorage", catalogueService.getCatalogue());
        catalogueService.sendPricesToShop(catalogueDto);
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
