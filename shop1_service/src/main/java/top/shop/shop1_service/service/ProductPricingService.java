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

    public void receiveProductPricingFromGateway(List<ProductPricingDto> ppDtoList) {
        ppDtoList.forEach(p -> {
                    if (productPricingExists(p.getProductServiceName())) {
                        updateProductPricing(p);
                    } else {
                        productPricingRepository.save(ProductPricing.builder()
                                .productServiceName(p.getProductServiceName())
                                .price(p.getPrice())
                                .build());
                    }
                }
        );
    }

    public ProductPricing getProductPricing(String productServiceName) {
        return productPricingRepository.findByProductServiceName(productServiceName).orElseThrow(
                () -> new ProductPricingException(HttpStatus.NOT_FOUND, "Product price with " + productServiceName + " not found!"));
    }

    public ProductPricingDto getProductPricingDto(String productServiceName) {
        return modelMapper.map(getProductPricing(productServiceName), ProductPricingDto.class);
    }

    public ProductPricing updateProductPricing(ProductPricingDto ppDto) {
        ProductPricing p = getProductPricing(ppDto.getProductServiceName());
        p.setPrice(ppDto.getPrice());

        return productPricingRepository.save(p);
    }

    public ProductDto updatePriceOfProductDto(ProductDto productDto) {
        productDto.setPrice(getProductPricingDto(productDto.getServiceName()).getPrice());
        return productDto;
    }

    public List<ProductPricingDto> getProductPricingDtoList() {
        return productPricingRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductPricingDto.class))
                .toList();
    }

    public boolean productPricingExists(String productServiceName) {
        return productPricingRepository.existsByProductServiceName(productServiceName);
    }

}
