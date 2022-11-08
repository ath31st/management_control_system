package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.ProductException;
import top.shop.backend.repository.ProductRepository;
import top.shop.backend.service.event.CatalogueEvent;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public String receiveProducts(List<Product> products) {
        products.forEach(p -> {
            if (productRepository.getProduct(p.getName()).isPresent()) {
                Product product = getProduct(p.getName());
                product.setAmount(product.getAmount() + p.getAmount());
                productRepository.save(product);
            } else {
                productRepository.save(p);
            }
        });

        eventPublisher.publishEvent(new CatalogueEvent(true));
        return "products received";
    }

    public Product getProduct(String productName) {
        return productRepository.getProduct(productName).orElseThrow(
                () -> new ProductException(HttpStatus.NOT_FOUND, "Product with name " + productName + " not found!"));
    }

    public void changeAmountProducts(int amount, String productName) {
        Product product = getProduct(productName);
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
