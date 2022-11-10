package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import top.shop.gateway.dto.UserDto;
import top.shop.gateway.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> userDtoList = userService.getUserDtoList();
        model.addAttribute("userDtoList", userDtoList);
        return "user-templates/users";
    }

    @GetMapping("/edit-user/{username}")
    public String userHandler(@PathVariable String username, Model model) {
        UserDto userDto = userService.getUserDto(username);
        model.addAttribute("userDto", userDto);
        return "user-templates/edit-user";
    }

    @PostMapping("/edit-user")
    public String userHandler(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "user-templates/edit-user";
        }
        model.addAttribute("userDto", userDto);

        userService.saveUserChanges(userDto);
        return "redirect:/users";
    }

}
