package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.ProductDto;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.exceptionhandler.exception.ProductException;
import top.shop.shop1_service.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public Product getProduct(String productName) {
        return productRepository.getProduct(productName).orElseThrow(
                () -> new ProductException(HttpStatus.NOT_FOUND, "Product with name " + productName + " not found!"));
    }

    public Product saveNewProduct(ProductDto productDto) {
        Product p = new Product();
        p.setAmount(productDto.getAmount());
        p.setCategory(productDto.getCategory());
        p.setName(productDto.getName());
        p.setPrice(0);
        return productRepository.save(p);
    }

    public Product updateAmountProduct(ProductDto productDto) {
        Product p = getProduct(productDto.getName());
        p.setAmount(productDto.getAmount());
        return productRepository.save(p);
    }

    public Product updatePriceProduct(ProductDto productDto) {
        Product p = getProduct(productDto.getName());
        p.setPrice(productDto.getPrice());
        return productRepository.save(p);
    }

    public List<ProductDto> getProductsWithPrice() {
        return productRepository.getProductWithPrice().stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    public boolean productExists(String productName) {
        return productRepository.existsByName(productName);
    }

}
