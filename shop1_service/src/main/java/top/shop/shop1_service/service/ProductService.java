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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public Product saveProductFromStorage(ProductDto dto, ProductPricing pp) {
        if (productRepository.existsByServiceName(dto.getServiceName()) && dto.getAmount() != 0) {
            Product product = getProduct(dto.getServiceName());
            product.setAmount(product.getAmount() + dto.getAmount());

            return productRepository.save(product);
        } else {
            Product p = new Product();
            p.setName(dto.getName());
            p.setServiceName(dto.getServiceName());
            p.setDescription(dto.getDescription());
            p.setAmount(dto.getAmount());
            p.setCategory(categoryService.categoryDtoToCategoryConverter(dto.getCategory()));
            p.setProductPricing(pp);

            return productRepository.save(p);
        }
    }

    public List<Product> getProductsByListServiceNames(List<String> listNames) {
        return productRepository.getProductsByList(listNames);
    }

    public void updateAmountProduct(ProductAmountDto pAmountDto) {
        if (productRepository.existsByServiceName(pAmountDto.getProductServiceName()))
            productRepository.updateAmountByServiceName(pAmountDto.getAmount(), pAmountDto.getProductServiceName());
    }

    public Product getProduct(String productServiceName) {
        return productRepository.findByServiceName(productServiceName).orElseThrow(
                () -> new ProductServiceException(HttpStatus.NOT_FOUND, "Product with service name: " + productServiceName + " not found!"));
    }

    public ProductDto productToDtoConverter(Product p) {
        ProductDto dto = new ProductDto();
        dto.setServiceName(p.getServiceName());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getProductPricing().getPrice());
        dto.setAmount(p.getAmount());
        dto.setCategory(categoryService.categoryToDtoConverter(p.getCategory()));
        return dto;
    }

    public long getAmountProductFromCatalogue(String productServiceName) {
        return productRepository.getAmountProductFromCatalogue(productServiceName);
    }

    public double getProductPrice(String productServiceName) {
        return productRepository.getProductPrice(productServiceName);
    }

    public boolean existsByServiceName(String productServiceName) {
        return productRepository.existsByServiceName(productServiceName);
    }
}
