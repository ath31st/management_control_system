package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.entity.Catalogue;
import top.shop.shop1_service.entity.Product;
import top.shop.shop1_service.entity.ProductPricing;
import top.shop.shop1_service.repository.CatalogueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${shop.service-name}")
    private String serviceName;
    private final ProductPricingService productPricingService;
    private final ProductService productService;
    private final CatalogueRepository catalogueRepository;
    private final ModelMapper modelMapper;

//    public CatalogueDto getCatalogueForCustomers() {
//        List<ProductDto> updatedProducts = getCatalogueDto().getProducts()
//                .stream()
//                .filter(p -> productPricingService.productPricingExists(p.getServiceName()) &&
//                        productPricingService.getProductPricingDto(p.getServiceName()).getPrice() != 0)
//                .map(productPricingService::updatePriceOfProductDto)
//                .sorted(Comparator.comparing(ProductDto::getServiceName))
//                .toList();
//
//        return CatalogueDto.builder()
//                .catalogueOnDate(LocalDateTime.now())
//                .products(updatedProducts)
//                .shopServiceName(serviceName)
//                .build();
//    }

    public void saveCatalogueFromStorage(CatalogueDto dto) {
        List<Product> products = dto.getProducts().stream()
                .map(pDto -> {
                    ProductPricing pp = productPricingService.addMockProductPricing(pDto);
                    Product product = productService.saveProductFromStorage(pDto, pp);
                    productPricingService.setProductInPp(pp, product);
                    return product;
                })
                .toList();

        Catalogue catalogue = new Catalogue();
        catalogue.setCatalogueOnDate(dto.getCatalogueOnDate());
        catalogue.setShopServiceName(dto.getShopServiceName());
        catalogue.setProducts(products);

        catalogueRepository.save(catalogue);
    }

    public Catalogue getCatalogue() {
        return catalogueRepository.findCatalogueByShopServiceName(serviceName)
                .orElse(Catalogue.builder()
                        .products(Collections.emptyList())
                        .shopServiceName(serviceName)
                        .catalogueOnDate(LocalDateTime.now())
                        .build());
    }

//    public CatalogueDto getCatalogueDto() {
//        Catalogue catalogue = getCatalogue();
//        CatalogueDto catalogueDto = modelMapper.map(catalogue, CatalogueDto.class);
//        catalogueDto.setProducts(catalogue.getProducts()
//                .values()
//                .stream()
//                .toList());
//        return catalogueDto;
//    }

    public List<String> getProductServiceNameList() {
        return getCatalogue()
                .getProducts()
                .stream()
                .map(Product::getServiceName)
                .toList();
    }

//    public long getAmountProductFromCatalogue(String productServiceName) {
//        return getCatalogue().getProducts().get(productServiceName).getAmount();
//    }

}

