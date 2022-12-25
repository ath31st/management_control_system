package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.discount.DiscountDto;
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

    @PostMapping("/new-discount")
    public ResponseEntity<HttpStatus> discountHandler(@RequestBody DiscountDto discountDto) {
        discountService.saveDiscount(discountDto);

        return ResponseEntity.ok().build();
    }
}
