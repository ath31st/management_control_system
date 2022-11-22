package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.shop.gateway.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        model.addAttribute("message", "Hello!");
        model.addAttribute("principal", principal);
       List<String> stringList = userService.getRoles();
        return "index";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/index";
    }

}
