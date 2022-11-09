package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.ShopService;
import top.shop.gateway.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ShopService shopService;

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

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> userDtoList = userService.getUserDtoList();
        model.addAttribute("userDtoList", userDtoList);
        return "users";
    }

}
