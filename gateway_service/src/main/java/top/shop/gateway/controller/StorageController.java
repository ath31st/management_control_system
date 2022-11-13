package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import top.shop.gateway.service.StorageService;
import top.shop.gateway.util.wrapper.ProductWrapper;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/storage")
    public String storageHandler(Model model) {
        model.addAttribute("wrapper", storageService.getProductWrapper());

        return "storage-templates/storage";
    }

    @PostMapping("/storage")
    public String storageHandler(@Valid ProductWrapper productWrapper, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("wrapper", storageService.getProductWrapper());
            return "storage-templates/storage";
        }

        storageService.sendProductWrapperToBackend(productWrapper);
        model.addAttribute("message", "Amount updated ");
        model.addAttribute("wrapper", storageService.getProductWrapper());

        return "storage-templates/storage";
    }
}
