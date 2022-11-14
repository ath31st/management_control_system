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
import top.shop.backend.util.wrapper.ProductWrapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void receiveProducts(ProductWrapper productWrapper) {
        productWrapper.getProductDtoList().forEach(p -> {
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

    public List<ProductDto> getProductDtoList() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    public Set<Product> convertProductSetFromDto(Set<ProductDto> productDtoList) {
        return productDtoList.stream()
                .map(pDto -> getProduct(pDto.getServiceName()))
                .collect(Collectors.toSet());
    }

    public Set<ProductDto> convertProductDtoSetFromProducts(Set<Product> products) {
        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .collect(Collectors.toSet());
    }

    public ProductWrapper getProductWrapper() {
        return new ProductWrapper(getProductDtoList());
    }
}
