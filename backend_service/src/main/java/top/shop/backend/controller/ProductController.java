package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.service.ProductService;
import top.shop.backend.util.wrapper.ProductWrapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public String productsHandler(@RequestBody ProductWrapper productWrapper) {
        return productService.receiveProducts(productWrapper);
    }

    @GetMapping("/products")
    public ResponseEntity<ProductWrapper> productsHandler() {
        return ResponseEntity.ok(productService.getProductWrapper());
    }

}
