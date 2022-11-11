package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.ProductDto;
import top.shop.shop1_service.dto.ProductPriceDto;
import top.shop.shop1_service.entity.ProductPrice;
import top.shop.shop1_service.exceptionhandler.exception.ProductPriceException;
import top.shop.shop1_service.repository.ProductPriceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductPriceRepository productPriceRepository;
    private final ModelMapper modelMapper;

    public void receiveProductPriceFromGateway(List<ProductPriceDto> productPriceDtoList) {
        productPriceDtoList.forEach(p -> {
                    if (productPriceExists(p.getProductName())) {
                        updateProductPrice(p);
                    } else {
                        productPriceRepository.save(ProductPrice.builder()
                                .productName(p.getProductName())
                                .price(p.getPrice())
                                .build());
                    }
                }
        );
    }

    private ProductPrice getProductPrice(String productName) {
        return productPriceRepository.findByProductName(productName).orElseThrow(
                () -> new ProductPriceException(HttpStatus.NOT_FOUND, "Product price with " + productName + " not found!"));
    }

    public ProductPrice updateProductPrice(ProductPriceDto productPriceDto) {
        ProductPrice p = getProductPrice(productPriceDto.getProductName());
        p.setPrice(productPriceDto.getPrice());

        return productPriceRepository.save(p);
    }

    public List<ProductPriceDto> getProductPriceDtoList() {
        return productPriceRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductPriceDto.class))
                .toList();
    }

    public boolean productPriceExists(String productName) {
        return productPriceRepository.existsByProductName(productName);
    }

}
