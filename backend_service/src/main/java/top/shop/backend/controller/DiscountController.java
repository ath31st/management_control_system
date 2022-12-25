package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shop.backend.service.DiscountService;
import top.shop.backend.util.wrapper.DiscountWrapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping("/discounts")
    public ResponseEntity<DiscountWrapper> getDiscounts() {
        return ResponseEntity.ok(discountService.getDiscountWrapper());
    }
}
