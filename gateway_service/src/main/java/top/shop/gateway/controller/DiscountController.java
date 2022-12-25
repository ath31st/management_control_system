package top.shop.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import top.shop.gateway.service.DiscountService;

@Controller
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;
}
