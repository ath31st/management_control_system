package top.shop.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.dto.ProductServiceNameDto;
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

    @PostMapping("/catalogue")
    public ResponseEntity<HttpStatus> catalogueHandler(@RequestBody CatalogueDto catalogueDto) {
        catalogueService.catalogueHandler(catalogueDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/catalogue-changes")
    public ResponseEntity<HttpStatus> catalogueChangesHandler(@RequestBody ProductServiceNameDto productServiceNameDto) {
        catalogueService.updateCatalogue(productServiceNameDto);
        return ResponseEntity.ok().build();
    }
}
