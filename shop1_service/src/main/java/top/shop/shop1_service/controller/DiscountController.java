package top.shop.shop1_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.shop.shop1_service.dto.discount.CommonDiscountDto;
import top.shop.shop1_service.dto.discount.DiscountDto;
import top.shop.shop1_service.dto.discount.PrivateDiscountDto;
import top.shop.shop1_service.service.CustomerService;
import top.shop.shop1_service.service.DiscountService;
import top.shop.shop1_service.util.wrapper.DiscountWrapper;

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
        return ResponseEntity.ok(customerService.getCustomersEmail());
    }

    @GetMapping("/discount/{productServiceName}")
    public ResponseEntity<DiscountDto> getDiscount(@PathVariable String productServiceName) {
        return ResponseEntity.ok(discountService.discountToDtoConverter(discountService.getDiscount(productServiceName)));
    }

    @PostMapping("/edit-discount")
    public ResponseEntity<DiscountDto> discountChangesHandler(@RequestBody DiscountDto discountDto) {
        discountService.saveDiscountChanges(discountDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/private-discount")
    public ResponseEntity<DiscountDto> getDiscount(@RequestParam(value = "name") String productServiceName,
                                                   @RequestParam(name = "email") String email) {
        return ResponseEntity.ok(discountService.privateDiscountToDtoConverter(discountService.getPrivateDiscount(productServiceName, email)));
    }

    @PostMapping("/edit-private-discount")
    public ResponseEntity<PrivateDiscountDto> discountChangesHandler(@RequestBody PrivateDiscountDto dto) {
        discountService.savePrivateDiscountChanges(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/common-discount/{productServiceName}")
    public ResponseEntity<CommonDiscountDto> getCommonDiscount(@PathVariable String productServiceName) {
        return ResponseEntity.ok(discountService.commonDiscountToDtoConverter(discountService.getCommonDiscount(productServiceName)));
    }

    @PostMapping("/edit-common-discount")
    public ResponseEntity<DiscountDto> discountChangesHandler(@RequestBody CommonDiscountDto dto) {
        discountService.saveCommonDiscountChanges(dto);

        return ResponseEntity.ok().build();
    }
}
