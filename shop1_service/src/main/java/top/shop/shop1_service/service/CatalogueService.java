package top.shop.shop1_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.config.kafkaconfig.CatalogueProducer;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.dto.product.ProductServiceNameDto;
import top.shop.shop1_service.entity.Catalogue;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.exceptionhandler.exception.CatalogueException;
import top.shop.shop1_service.repository.CatalogueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${shop.service-name}")
    private String serviceName;
    private final ProductPricingService productPricingService;
    private final ProductService productService;
    private final DiscountService discountService;
    private final CatalogueRepository catalogueRepository;
    private final CatalogueProducer catalogueProducer;


    public Catalogue getCatalogue(String shopServiceName) {
        return catalogueRepository.findCatalogueByShopServiceName(shopServiceName).orElseThrow(
                () -> new CatalogueException(HttpStatus.NOT_FOUND, "Catalogue for shop with name " + shopServiceName + " not found!")
        );
    }

    public CatalogueDto getCatalogueForCustomers() {
        CatalogueDto catalogueDto = getCatalogueDto();
        List<ProductDto> productDtoList = catalogueDto.getProducts().stream()
                .filter(p -> p.getPrice() != 0)
                .toList();
        catalogueDto.setProducts(productDtoList);
        catalogueDto.setDiscounts(discountService.getActiveDiscountDtoList());
        return catalogueDto;
    }

    public void saveCatalogueFromDto(CatalogueDto dto) {
        if (catalogueRepository.existsByShopServiceName(dto.getShopServiceName()))
            catalogueRepository.delete(getCatalogue(dto.getShopServiceName()));

        List<Product> products = dto.getProducts().stream()
                .map(pDto -> {
                    ProductPricing pp = productPricingService.addMockProductPricing(pDto);
                    Product product = productService.saveProductFromStorage(pDto, pp);
                    productPricingService.setProductInPp(pp, product);
                    return product;
                })
                .toList();

        Catalogue catalogue = getEmptyCatalogue();
        catalogue.setCatalogueOnDate(dto.getCatalogueOnDate());
        catalogue.setShopServiceName(dto.getShopServiceName());
        catalogue.setProducts(products);


        catalogueRepository.save(catalogue);
        sendCatalogue(getCatalogueDto());
    }

    public Catalogue getEmptyCatalogue() {
        return catalogueRepository.findCatalogueByShopServiceName(serviceName)
                .orElse(Catalogue.builder()
                        .products(Collections.emptyList())
                        .shopServiceName(serviceName)
                        .catalogueOnDate(LocalDateTime.now())
                        .build());
    }

    public CatalogueDto getCatalogueDto() {
        Catalogue catalogue = getEmptyCatalogue();
        return CatalogueDto.builder()
                .catalogueOnDate(catalogue.getCatalogueOnDate())
                .shopServiceName(catalogue.getShopServiceName())
                .products(catalogue.getProducts().stream()
                        .map(productService::productToDtoConverter)
                        .sorted(Comparator.comparing(ProductDto::getServiceName))
                        .toList())
                .build();
    }

    public List<String> getProductServiceNameList() {
        return getEmptyCatalogue()
                .getProducts()
                .stream()
                .map(Product::getServiceName)
                .toList();
    }

    public void updateCatalogue(ProductServiceNameDto productServiceNameDto) {
        Catalogue catalogue = getCatalogue(productServiceNameDto.getShopServiceName());

        List<Product> products = new java.util.ArrayList<>(catalogue.getProducts()
                .stream()
                .filter(p -> !productServiceNameDto.getDeleteProductServiceNames().contains(p.getServiceName()))
                .toList());
        products.addAll(productService.getProductsByListServiceNames(productServiceNameDto.getAddProductServiceNames()));
        products.sort(Comparator.comparing(Product::getServiceName));

        catalogue.setProducts(products);

        catalogueRepository.save(catalogue);
        sendCatalogue(getCatalogueDto());
    }

    public long getAmountProductFromCatalogue(String productServiceName) {
        return productService.getAmountProductFromCatalogue(productServiceName);
    }

    private void sendCatalogue(CatalogueDto dto) {
        try {
            catalogueProducer.sendMessage(dto);
        } catch (JsonProcessingException e) {
            throw new CatalogueException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}

