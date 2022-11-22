package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import top.shop.gateway.dto.ShopDto;
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

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> userDtoList = userService.getUserDtoList();
        model.addAttribute("userDtoList", userDtoList);
        return "user-templates/users";
    }

    @GetMapping("/edit-user/{username}")
    public String userHandler(@PathVariable String username, Model model) {
        UserDto userDto = userService.getUserDto(username);
        List<ShopDto> shopList = shopService.getShops();
        List<String> roles = userService.getRoles();

        model.addAttribute("shopList", shopList);
        model.addAttribute("roleList", roles);
        model.addAttribute("userDto", userDto);
        return "user-templates/edit-user";
    }

    @PostMapping("/edit-user")
    public String userHandler(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model) {
        List<ShopDto> shopList = shopService.getShops();
        List<String> roles = userService.getRoles();

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            model.addAttribute("shopList", shopList);
            model.addAttribute("roleList", roles);
            return "user-templates/edit-user";
        }
        model.addAttribute("userDto", userDto);

        return "redirect:/users";
    }

    @GetMapping("/delete-user/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return "redirect:/users";
    }

}
