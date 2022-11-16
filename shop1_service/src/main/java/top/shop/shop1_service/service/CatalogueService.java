package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.product.ProductDto;
import top.shop.shop1_service.entity.Catalogue;
import top.shop.shop1_service.exceptionhandler.exception.CatalogueException;
import top.shop.shop1_service.repository.CatalogueRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
        Set<ProductDto> updatedProducts = getCatalogueDto().getProducts()
                .stream()
                .filter(p -> productPricingService.productPricingExists(p.getServiceName()) &&
                        productPricingService.getProductPricingDto(p.getServiceName()).getPrice() != 0)
                .map(productPricingService::updatePriceOfProductDto)
                .collect(Collectors.toSet());

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
                .products(catalogueDto.getProducts())
                .build();

        catalogueRepository.save(catalogue);
        productPricingService.addMockProductPricing(catalogueDto);
    }

    public Catalogue getCatalogue() {
        return catalogueRepository.findCatalogueByShopServiceName(serviceName)
                .orElse(Catalogue.builder()
                        .products(Collections.emptySet())
                        .shopServiceName(serviceName)
                        .catalogueOnDate(LocalDateTime.now())
                        .build());
    }

    public CatalogueDto getCatalogueDto() {
        return modelMapper.map(getCatalogue(), CatalogueDto.class);
    }

    public List<String> getProductServiceNameList() {
        return getCatalogue().getProducts()
                .stream()
                .map(ProductDto::getServiceName)
                .toList();
    }

}
