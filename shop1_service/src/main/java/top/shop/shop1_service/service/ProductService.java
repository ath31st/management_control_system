package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
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

    public List<ProductDto> getProductsWithPrice() {
        return productRepository.getProductWithPrice().stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .toList();
    }

    public void receiveCatalogueFromBackend(CatalogueDto catalogueDto) {
        if (catalogueDto == null) return;

        catalogueDto.getProducts()
                .forEach(pDto -> {
                    if (productRepository.getProduct(pDto.getName()).isPresent()) {
                        Product p = getProduct(pDto.getName());
                        p.setAmount(pDto.getAmount());
                        productRepository.save(p);
                    } else {
                        Product p = new Product();
                        p.setAmount(pDto.getAmount());
                        p.setCategory(pDto.getCategory());
                        p.setName(pDto.getName());
                        p.setPrice(0);
                        productRepository.save(p);
                    }
                });
    }
}
