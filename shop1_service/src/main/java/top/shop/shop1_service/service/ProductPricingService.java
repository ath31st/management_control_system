package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.dto.product.ProductPricingDto;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.ProductPricingException;
import top.shop.shop1_service.repository.ProductPricingRepository;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPricingService {
    private final ProductPricingRepository productPricingRepository;
    private final ProductService productService;

    public void receiveProductPricingWrapperFromGateway(ProductPricingWrapper wrapper) {
        wrapper.getPricingDtoList().forEach(p -> {
                    if (productPricingExists(p.getProductServiceName())) {
                        updateProductPricing(p);
                    } else {
                        ProductPricing pp = new ProductPricing();
                        pp.setPrice(p.getPrice());
                        pp.setProduct(productService.getProduct(p.getProductServiceName()));
                        productPricingRepository.save(pp);
                    }
                }
        );
    }

    public ProductPricing getProductPricing(String productServiceName) {
        Optional<ProductPricing> pp = productPricingRepository.findByProduct_ServiceName(productServiceName);
        return pp.orElseThrow(() -> new ProductPricingException(HttpStatus.NOT_FOUND, "Product price with " + productServiceName + " not found!"));
    }

    public ProductPricingDto getProductPricingDto(String productServiceName) {
        return productPricingToDtoConverter(getProductPricing(productServiceName));
    }

    public void updateProductPricing(ProductPricingDto ppDto) {
        ProductPricing pp = getProductPricing(ppDto.getProductServiceName());
        pp.setPrice(ppDto.getPrice());
        productPricingRepository.save(pp);
    }

    public List<ProductPricingDto> getProductPricingDtoList(List<String> productServiceName) {
        return productPricingRepository.findAll().stream()
                .filter(pp -> productServiceName.contains(pp.getProduct().getServiceName()))
                .map(this::productPricingToDtoConverter)
                .sorted(Comparator.comparing(ProductPricingDto::getProductServiceName))
                .toList();
    }

    public ProductPricing addMockProductPricing(ProductDto productDto) {
        if (productPricingRepository.existsByProduct_ServiceName(productDto.getServiceName())) {
            return getProductPricing(productDto.getServiceName());
        } else {
            ProductPricing pp = new ProductPricing();
            pp.setPrice(0);
            return productPricingRepository.save(pp);
        }
    }

    public void setProductInPp(ProductPricing pp, Product product) {
        if (pp.getProduct() == null) {
            pp.setProduct(product);
            productPricingRepository.save(pp);
        }
    }

    public boolean productPricingExists(String productServiceName) {
        return productPricingRepository.existsByProduct_ServiceName(productServiceName);
    }

    private ProductPricingDto productPricingToDtoConverter(ProductPricing pp) {
        ProductPricingDto dto = new ProductPricingDto();
        dto.setPrice(pp.getPrice());
        dto.setProductServiceName(pp.getProduct().getServiceName());
        return dto;
    }

    public double getProductPrice(String productServiceName) {
        return
    }
}
