package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.backend.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
}
