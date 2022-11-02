package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.backend.entity.Product;
import top.shop.backend.repository.ProductRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String receiveProducts(List<Product> products) {
        productRepository.saveAll(products);
        return "products received";
    }

}
