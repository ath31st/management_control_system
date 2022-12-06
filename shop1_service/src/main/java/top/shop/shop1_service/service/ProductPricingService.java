package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductAmountDto;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.dto.product.ProductPricingDto;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.ProductPricingException;
import top.shop.shop1_service.repository.ProductPricingRepository;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPricingService {

    private final ProductPricingRepository productPricingRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    public void receiveProductPricingWrapperFromGateway(ProductPricingWrapper wrapper) {
        wrapper.getPricingDtoList().forEach(p -> {
                    if (productPricingExists(p.getProductServiceName())) {
                        updateProductPricing(p);
                    } else {
                        productPricingRepository.save(ProductPricing.builder()
                                .productServiceName(p.getProductServiceName())
                                .price(p.getPrice())
                                .build());
                    }
                }
        );
    }

    public ProductPricing getProductPricing(String productServiceName) {
        return productPricingRepository.findByProductServiceName(productServiceName).orElseThrow(
                () -> new ProductPricingException(HttpStatus.NOT_FOUND, "Product price with " + productServiceName + " not found!"));
    }

    public ProductPricingDto getProductPricingDto(String productServiceName) {
        return modelMapper.map(getProductPricing(productServiceName), ProductPricingDto.class);
    }

    public void updateProductPricing(ProductPricingDto ppDto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("productServiceName").is(ppDto.getProductServiceName()));
        Update update = new Update();
        update.set("price", ppDto.getPrice());
        mongoTemplate.updateFirst(query, update, ProductPricing.class);
    }

    public ProductDto updatePriceOfProductDto(ProductDto productDto) {
        productDto.setPrice(getProductPricingDto(productDto.getServiceName()).getPrice());
        return productDto;
    }

    public List<ProductPricingDto> getProductPricingDtoList(List<String> productServiceName) {
        return productPricingRepository.findAll().stream()
                .filter(pp -> productServiceName.contains(pp.getProductServiceName()))
                .map(pp -> modelMapper.map(pp, ProductPricingDto.class))
                .sorted(Comparator.comparing(ProductPricingDto::getProductServiceName))
                .toList();
    }

    public void addMockProductPricing(CatalogueDto catalogueDto) {
        catalogueDto.getProducts()
                .stream()
                .filter(p -> !productPricingExists(p.getServiceName()))
                .forEach(p -> {
                    ProductPricing pp = new ProductPricing();
                    pp.setProductServiceName(p.getServiceName());
                    pp.setPrice(0);
                    productPricingRepository.save(pp);
                });
    }

    public boolean productPricingExists(String productServiceName) {
        return productPricingRepository.existsByProductServiceName(productServiceName);
    }

}
