package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.product.ProductAmountDto;
import top.shop.backend.dto.product.ProductDto;
import top.shop.backend.entity.Category;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.ProductException;
import top.shop.backend.repository.ProductRepository;
import top.shop.backend.service.event.CategoryEvent;
import top.shop.backend.service.event.ProductAmountEvent;
import top.shop.backend.util.wrapper.ProductWrapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

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
            if (productRepository.getProduct(p.getServiceName()).isPresent() && p.getAmount() != 0) {
                Product product = getProduct(p.getServiceName());
                product.setAmount(product.getAmount() + p.getAmount());

                productRepository.save(product);
                eventPublisher.publishEvent(new ProductAmountEvent(
                        new ProductAmountDto(product.getAmount(), product.getServiceName(), LocalDateTime.now())));
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
                eventPublisher.publishEvent(new ProductAmountEvent(
                        new ProductAmountDto(product.getAmount(), product.getServiceName(), LocalDateTime.now())));
            }
        });
    }

    public Product getProduct(String productServiceName) {
        return productRepository.getProduct(productServiceName).orElseThrow(
                () -> new ProductException(HttpStatus.NOT_FOUND, "Product with name " + productServiceName + " not found!"));
    }

    public void reduceAmountProduct(int amount, String productServiceName) {
        Product product = getProduct(productServiceName);
        product.setAmount(product.getAmount() - amount);

        productRepository.save(product);
        eventPublisher.publishEvent(new ProductAmountEvent(
                new ProductAmountDto(product.getAmount(), product.getServiceName(), LocalDateTime.now())));
    }

    public void increaseAmountProduct(int amount, String productServiceName) {
        Product product = getProduct(productServiceName);
        product.setAmount(product.getAmount() + amount);

        productRepository.save(product);
        eventPublisher.publishEvent(new ProductAmountEvent(
                new ProductAmountDto(product.getAmount(), product.getServiceName(), LocalDateTime.now())));
    }

    public List<ProductDto> getProductDtoList() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(p -> modelMapper.map(p, ProductDto.class))
                .sorted(Comparator.comparing(ProductDto::getServiceName))
                .toList();
    }

    public List<Product> convertProductListFromDto(List<ProductDto> productDtoList) {
        return productDtoList.stream()
                .map(pDto -> getProduct(pDto.getServiceName()))
                .sorted(Comparator.comparing(Product::getServiceName))
                .toList();
    }

    public List<ProductDto> convertProductDtoListFromProducts(List<Product> products) {
        return products.stream()
                .map(this::convertProductToProductDto)
                .sorted(Comparator.comparing(ProductDto::getServiceName))
                .toList();
    }

    public ProductWrapper getProductWrapper() {
        return new ProductWrapper(getProductDtoList());
    }

    public List<Product> getProductsByListServiceNames(List<String> productServiceNames) {
        return productRepository.findAll()
                .stream()
                .filter(p -> productServiceNames.contains(p.getServiceName()))
                .toList();
    }

    public ProductWrapper getProductWrapperWithoutCatalogue(List<String> catalogueProductNames) {
        List<Product> products = productRepository.findAll();
        products.sort(Comparator.comparing(Product::getServiceName));

        return new ProductWrapper(products.stream()
                .filter(p -> !catalogueProductNames.contains(p.getServiceName()))
                .map(this::convertProductToProductDto)
                .toList());
    }

    public ProductDto getProductDto(String productServiceName) {
        Product p = getProduct(productServiceName);
        return convertProductToProductDto(p);
    }

    public ProductDto changeProduct(ProductDto productDto) {
        Product product = getProduct(productDto.getServiceName());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(categoryService.getCategory(productDto.getCategory().getServiceName()));
        product.setPrice(productDto.getPrice());
        product.setAmount(productDto.getAmount());

        productRepository.save(product);
        eventPublisher.publishEvent(new ProductAmountEvent(
                new ProductAmountDto(product.getAmount(), product.getServiceName(), LocalDateTime.now())));

        return convertProductToProductDto(product);
    }

    @EventListener
    public void changeProductsCategoryToDefault(CategoryEvent event) {
        List<Product> products = productRepository.getProductByCategory_ServiceName((String) event.getSource());
        products.forEach(p -> {
            p.setCategory(categoryService.getCategory("default_category"));
            productRepository.save(p);
        });
    }

    private ProductDto convertProductToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
