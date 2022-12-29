package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.product.ProductAmountDto;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.ProductServiceException;
import top.shop.shop1_service.repository.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Product saveProductFromStorage(ProductDto dto, ProductPricing pp) {
        Product p = new Product();
        p.setName(dto.getName());
        p.setServiceName(dto.getServiceName());
        p.setDescription(dto.getDescription());
        p.setAmount(dto.getAmount());
        p.setCategory(categoryService.getCategory(dto.getCategory()));
        p.setProductPricing(pp);

        return productRepository.save(p);
    }

    public void updateAmountProduct(ProductAmountDto pAmountDto) {
        if (productRepository.existsByServiceName(pAmountDto.getProductServiceName())) {
            Product product = getProduct(pAmountDto.getProductServiceName());
            product.setAmount(pAmountDto.getAmount());
        }
    }

    public Product getProduct(String productServiceName) {
        return productRepository.findByServiceName(productServiceName).orElseThrow(
                () -> new ProductServiceException(HttpStatus.NOT_FOUND, "Product with service name: " + productServiceName + " not found!"));
    }
}
