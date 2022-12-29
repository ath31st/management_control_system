package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.dto.product.ProductPricingDto;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.repository.ProductPricingRepository;
import top.shop.shop1_service.util.wrapper.ProductPricingWrapper;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPricingService {
    private final ProductPricingRepository productPricingRepository;
    private final ProductService productService;

    public void receiveProductPricingWrapperFromGateway(ProductPricingWrapper wrapper) {
//        wrapper.getPricingDtoList().forEach(p -> {
//                    if (productPricingExists(p.getProductServiceName())) {
//                        updateProductPricing(p);
//                    } else {
//                        mongoTemplate.save(ProductPricing.builder()
//                                .productServiceName(p.getProductServiceName())
//                                .price(p.getPrice())
//                                .build());
//                    }
//                }
//        );
    }

//    public ProductPricing getProductPricing(String productServiceName) {
//        Optional<ProductPricing> pp = Optional.ofNullable(mongoTemplate.findById(productServiceName, ProductPricing.class));
//        return pp.orElseThrow(() -> new ProductPricingException(HttpStatus.NOT_FOUND, "Product price with " + productServiceName + " not found!"));
//    }

//    public ProductPricingDto getProductPricingDto(String productServiceName) {
//        return modelMapper.map(getProductPricing(productServiceName), ProductPricingDto.class);
//    }

//    public void updateProductPricing(ProductPricingDto ppDto) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("productServiceName").is(ppDto.getProductServiceName()));
//        Update update = new Update();
//        update.set("price", ppDto.getPrice());
//        mongoTemplate.findAndModify(query, update, ProductPricing.class);
//    }

//    public ProductDto updatePriceOfProductDto(ProductDto productDto) {
//        productDto.setPrice(getProductPricingDto(productDto.getServiceName()).getPrice());
//        return productDto;
//    }

    public List<ProductPricingDto> getProductPricingDtoList(List<String> productServiceName) {
        return productPricingRepository.findAll().stream()
                .filter(pp -> productServiceName.contains(pp.getProduct().getServiceName()))
                .map(this::productPricingMapper)
                .sorted(Comparator.comparing(ProductPricingDto::getProductServiceName))
                .toList();
    }

    public ProductPricing addMockProductPricing(ProductDto productDto) {
        if (productPricingRepository.existsByProduct_ServiceName(productDto.getServiceName())) {
            return productPricingRepository.findByProduct_ServiceName(productDto.getServiceName());
        } else {
            ProductPricing pp = new ProductPricing();
            pp.setPrice(0);
            return productPricingRepository.save(pp);
        }
    }

//    public boolean productPricingExists(String productServiceName) {
//        return mongoTemplate.exists(Query.query(Criteria.where("productServiceName")
//                .is(productServiceName)), ProductPricing.class);
//    }

    private ProductPricingDto productPricingMapper(ProductPricing pp) {
        ProductPricingDto dto = new ProductPricingDto();
        dto.setPrice(pp.getPrice());
        dto.setProductServiceName(pp.getProduct().getServiceName());
        return dto;
    }

}
