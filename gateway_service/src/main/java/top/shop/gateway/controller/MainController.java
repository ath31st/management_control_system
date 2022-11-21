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
import top.shop.gateway.service.UserService;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", "Hello!");
        return "index";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/login-error")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        return "login";
//    }

//    @GetMapping("/register")
//    public String register(Model model) {
//        model.addAttribute("userData", new UserDto());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String userRegistration(@Valid @ModelAttribute("userData") UserDto userData, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("userData", userData);
//            return "register";
//        }
//        try {
//            userService.registerUser(userData);
//        } catch (ResponseStatusException e) {
//            bindingResult.rejectValue("username", "userData.username", "An account already exists for this username.");
//            model.addAttribute("userData", userData);
//            return "register";
//        }
//        return "redirect:/index";
//    }

}
