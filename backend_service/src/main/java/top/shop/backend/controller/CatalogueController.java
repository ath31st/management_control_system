package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.service.CatalogueService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CatalogueController {

    private final CatalogueService catalogueService;

    @GetMapping("/catalogue/{shopServiceName}")
    public ResponseEntity<CatalogueDto> catalogue(@PathVariable String shopServiceName) {
        return ResponseEntity.ok(catalogueService.getCatalogueDto(shopServiceName));
    }
}
