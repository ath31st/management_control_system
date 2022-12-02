package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.CategoryDto;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> categoriesHandler() {
        return ResponseEntity.ok(categoryService.getListCategoryDto());
    }

    @GetMapping("/categories/{categoryServiceName}")
    public ResponseEntity<CategoryDto> category(@PathVariable String categoryServiceName) {
        return ResponseEntity.ok(categoryService.getCategoryDto(categoryServiceName));
    }

    @PostMapping("/new-category")
    public ResponseEntity<HttpStatus> categoryHandler(@RequestBody CategoryDto categoryDto) {
        categoryService.saveCategory(categoryDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit-category")
    public ResponseEntity<HttpStatus> categoryChanges(@RequestBody CategoryDto categoryDto) {
        categoryService.saveChangesCategory(categoryDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-category/{categoryServiceName}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable String categoryServiceName) {
        categoryService.deleteCategory(categoryServiceName);

        return ResponseEntity.ok().build();
    }
}
