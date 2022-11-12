package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.service.ShopService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/shops")
    public ResponseEntity<List<ShopDto>> shopsHandler() {
        return ResponseEntity.ok(shopService.getShopDtoList());
    }

    @GetMapping("/shops/{shopServiceName}")
    public ResponseEntity<ShopDto> shop(@PathVariable String shopServiceName) {
        return ResponseEntity.ok(shopService.getShopDto(shopServiceName));
    }

    @PostMapping("/new-shop")
    public ResponseEntity<HttpStatus> shopHandler(@RequestBody ShopDto shopDto) {
        return shopService.saveNewShop(shopDto);
    }

    @PostMapping("/edit-shop")
    public ResponseEntity<HttpStatus> shopChanges(@RequestBody ShopDto shopDto) {
        return shopService.saveShopChanges(shopDto);
    }
}
