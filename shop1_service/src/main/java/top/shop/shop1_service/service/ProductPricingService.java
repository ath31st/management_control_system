package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.ProductDto;
import top.shop.shop1_service.dto.ProductPricingDto;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.ProductPricingException;
import top.shop.shop1_service.repository.ProductPricingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPricingService {

    private final ProductPricingRepository productPricingRepository;
    private final ModelMapper modelMapper;

    public void receiveProductPricingFromGateway(List<ProductPricingDto> productPricingDtoList) {
        productPricingDtoList.forEach(p -> {
                    if (productPricingExists(p.getProductName())) {
                        updateProductPricing(p);
                    } else {
                        productPricingRepository.save(ProductPricing.builder()
                                .productName(p.getProductName())
                                .price(p.getPrice())
                                .build());
                    }
                }
        );
    }

    public ProductPricing getProductPricing(String productName) {
        return productPricingRepository.findByProductName(productName).orElseThrow(
                () -> new ProductPricingException(HttpStatus.NOT_FOUND, "Product price with " + productName + " not found!"));
    }

    public ProductPricingDto getProductPricingDto(String productName) {
        return modelMapper.map(getProductPricing(productName), ProductPricingDto.class);
    }

    public ProductPricing updateProductPricing(ProductPricingDto productPricingDto) {
        ProductPricing p = getProductPricing(productPricingDto.getProductName());
        p.setPrice(productPricingDto.getPrice());

        return productPricingRepository.save(p);
    }

    public ProductDto updatePriceOfProductDto(ProductDto productDto) {
        productDto.setPrice(getProductPricingDto(productDto.getName()).getPrice());
        return productDto;
    }

    public List<ProductPricingDto> getProductPricingDtoList() {
        return productPricingRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductPricingDto.class))
                .toList();
    }

    public boolean productPricingExists(String productName) {
        return productPricingRepository.existsByProductName(productName);
    }

}
