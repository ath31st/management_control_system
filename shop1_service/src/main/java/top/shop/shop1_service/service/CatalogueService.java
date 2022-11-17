package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductAmountDto;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.entity.Catalogue;
import top.shop.shop1_service.repository.CatalogueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    @Value("${shop.service-name}")
    private String serviceName;
    private final ProductPricingService productPricingService;
    private final CatalogueRepository catalogueRepository;
    private final ModelMapper modelMapper;

    public CatalogueDto getCatalogueForCustomers() {
        List<ProductDto> updatedProducts = getCatalogueDto().getProducts()
                .stream()
                .filter(p -> productPricingService.productPricingExists(p.getServiceName()) &&
                        productPricingService.getProductPricingDto(p.getServiceName()).getPrice() != 0)
                .map(productPricingService::updatePriceOfProductDto)
                .sorted(Comparator.comparing(ProductDto::getServiceName))
                .toList();

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(updatedProducts)
                .shopServiceName(serviceName)
                .build();
    }

    public void saveCatalogueFromStorage(CatalogueDto catalogueDto) {
        Catalogue catalogue = Catalogue.builder()
                .catalogueOnDate(catalogueDto.getCatalogueOnDate())
                .shopServiceName(catalogueDto.getShopServiceName())
                .products(catalogueDto.getProducts()
                        .stream()
                        .collect(Collectors.toMap(ProductDto::getServiceName, Function.identity())))
                .build();

        catalogueRepository.save(catalogue);
        productPricingService.addMockProductPricing(catalogueDto);
    }

    public Catalogue getCatalogue() {
        return catalogueRepository.findCatalogueByShopServiceName(serviceName)
                .orElse(Catalogue.builder()
                        .products(Collections.emptyMap())
                        .shopServiceName(serviceName)
                        .catalogueOnDate(LocalDateTime.now())
                        .build());
    }

    public CatalogueDto getCatalogueDto() {
        Catalogue catalogue = getCatalogue();
        CatalogueDto catalogueDto = modelMapper.map(catalogue, CatalogueDto.class);
        catalogueDto.setProducts(catalogue.getProducts()
                .values()
                .stream()
                .toList());
        return catalogueDto;
    }

    public List<String> getProductServiceNameList() {
        return getCatalogue().getProducts()
                .values()
                .stream()
                .map(ProductDto::getServiceName)
                .toList();
    }

    public void updateAmountProductInCatalogue(ProductAmountDto pAmountDto) {
        Catalogue catalogue = getCatalogue();
        if (catalogue.getProducts().isEmpty()) return;

        Map<String, ProductDto> products = catalogue.getProducts();
        products.computeIfPresent(pAmountDto.getProductServiceName(), (s, pDto) -> {
            pDto.setAmount(pAmountDto.getAmount());
            return pDto;
        });
        catalogue.setProducts(products);

        catalogueRepository.save(catalogue);
    }

}

