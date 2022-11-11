package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.entity.Product;
import top.shop.backend.service.ProductService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public String productsHandler(@RequestBody List<ProductDto> products) {
        return productService.receiveProducts(products);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> productsHandler() {
        return ResponseEntity.ok(productService.getListProductDto());
    }

}
