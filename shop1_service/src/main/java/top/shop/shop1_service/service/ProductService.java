package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductPricingService productPricingService;

    public Product saveProduct(ProductDto dto) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setServiceName(dto.getServiceName());
        p.setDescription(dto.getDescription());
        p.setAmount(dto.getAmount());
        p.setCategory(categoryService.getCategory(dto.getCategory()));
        p.setProductPricing(productPricingService.addMockProductPricing(dto));
        return productRepository.save(p);
    }
}
