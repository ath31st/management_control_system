package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.service.CustomerService;
import top.shop.shop1_service.service.DiscountService;
import top.shop.shop1_service.util.wrapper.DiscountWrapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiscountController {
    private final DiscountService discountService;
    private final CustomerService customerService;

    @GetMapping("/discounts")
    public ResponseEntity<DiscountWrapper> getDiscounts() {
        return ResponseEntity.ok(discountService.getDiscountWrapper());
    }

    @PostMapping("/new-discount")
    public ResponseEntity<HttpStatus> discountHandler(@RequestBody DiscountWrapper discountWrapper) {
        if (discountWrapper.getDiscountList() != null)
            discountWrapper.getDiscountList().forEach(discountService::saveDiscount);

        if (discountWrapper.getPrivateDiscountList() != null)
            discountWrapper.getPrivateDiscountList().forEach(discountService::savePrivateDiscount);

        if (discountWrapper.getCommonDiscountList() != null)
            discountWrapper.getCommonDiscountList().forEach(discountService::saveCommonDiscount);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/customers")
    public ResponseEntity<String[]> getCustomersList() {
        return ResponseEntity.ok(customerService.getCustomersUsername());
    }
}
