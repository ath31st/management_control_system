package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.service.ShopService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> productsHandler() {
        return ResponseEntity.ok(shopService.getShopDtoList());
    }

}
