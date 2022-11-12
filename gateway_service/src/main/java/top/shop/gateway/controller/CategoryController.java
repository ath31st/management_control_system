package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import top.shop.gateway.dto.CategoryDto;
import top.shop.gateway.service.CategoryService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model) {
        List<CategoryDto> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        return "category-templates/categories";
    }

    @GetMapping("/edit-category/{categoryServiceName}")
    public String categoryChangesHandler(@PathVariable String categoryServiceName, Model model) {
        CategoryDto categoryDto = categoryService.getCategory(categoryServiceName);
        model.addAttribute("categoryDto", categoryDto);
        return "category-templates/edit-category";
    }

    @PostMapping("/edit-category")
    public String categoryChangesHandler(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "category-templates/edit-category";
        }
        model.addAttribute("categoryDto", categoryDto);

        categoryService.sendCategoryChanges(categoryDto);
        return "redirect:/categories";
    }

    @GetMapping("/new-category")
    public String categoryHandler(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "category-templates/new-category";
    }

    @PostMapping("/new-category")
    public String categoryHandler(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "category-templates/new-category";
        }
        try {
            categoryService.createCategory(categoryDto);
        } catch (HttpClientErrorException e) {
            bindingResult.rejectValue("serviceName", "categoryDto.serviceName", "Category already exists for this service name.");
            model.addAttribute("categoryDto", categoryDto);
            return "category-templates/new-category";
        }

        return "redirect:/categories";
    }

}
