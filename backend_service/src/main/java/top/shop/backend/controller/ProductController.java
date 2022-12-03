package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.product.ProductDto;
import top.shop.backend.service.ProductService;
import top.shop.backend.util.wrapper.ProductWrapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<HttpStatus> productsHandler(@RequestBody ProductWrapper productWrapper) {
        productService.receiveProducts(productWrapper);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/products-without-catalogue")
    public ResponseEntity<ProductWrapper> productsHandler(@RequestBody List<String> catalogueProductNames) {

        return ResponseEntity.ok(productService.getProductWrapperWithoutCatalogue(catalogueProductNames));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductWrapper> productsHandler() {
        return ResponseEntity.ok(productService.getProductWrapper());
    }

    @GetMapping("/products/{productServiceName}")
    public ResponseEntity<ProductDto> productHandler(@PathVariable String productServiceName) {
        return ResponseEntity.ok(productService.getProductDto(productServiceName));
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDto> productHandler(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.changeProduct(productDto));
    }

}
