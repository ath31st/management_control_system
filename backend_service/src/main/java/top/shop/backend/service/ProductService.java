package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.ProductException;
import top.shop.backend.repository.ProductRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public String receiveProducts(List<Product> products) {
        productRepository.saveAll(products);
        return "products received";
    }

    public Product getProduct(String productName) {
        return productRepository.getProduct(productName).orElseThrow(
                () -> new ProductException(HttpStatus.NOT_FOUND, "Product with name " + productName + " not found!"));
    }

    public double getTotalPrice(int amount, String productName) {
        return getProduct(productName).getPrice() * (double) amount;
    }

    public List<ProductDto> getListProductDto() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

}
