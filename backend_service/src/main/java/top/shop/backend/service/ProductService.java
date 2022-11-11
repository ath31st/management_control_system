package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.entity.Category;
import top.shop.backend.entity.Product;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.exceptionhandler.exception.ProductException;
import top.shop.backend.repository.ProductRepository;
import top.shop.backend.service.event.CatalogueEvent;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public String receiveProducts(List<ProductDto> products) {
        products.forEach(p -> {
            if (productRepository.getProduct(p.getServiceName()).isPresent()) {
                Product product = getProduct(p.getServiceName());
                product.setAmount(product.getAmount() + p.getAmount());

                productRepository.save(product);
            } else {
                Category category;
                if (categoryService.categoryExists(p.getCategory().getServiceName())) {
                    category = categoryService.getCategory(p.getCategory().getServiceName());
                } else {
                    category = categoryService.saveCategory(p.getCategory());
                }

                Product product = Product.builder()
                        .name(p.getName())
                        .serviceName(p.getServiceName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .amount(p.getAmount())
                        .category(category)
                        .build();

                productRepository.save(product);
            }
        });

        eventPublisher.publishEvent(new CatalogueEvent(true));
        return "products received";
    }

    public Product getProduct(String productServiceName) {
        return productRepository.getProduct(productServiceName).orElseThrow(
                () -> new ProductException(HttpStatus.NOT_FOUND, "Product with name " + productServiceName + " not found!"));
    }

    public void changeAmountProduct(int amount, String productServiceName) {
        Product product = getProduct(productServiceName);
        product.setAmount(product.getAmount() - amount);

        productRepository.save(product);
    }

    public List<ProductDto> getListProductDto() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

}
