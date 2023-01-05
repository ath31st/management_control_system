package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.dto.product.ProductDto;
import top.shop.backend.entity.Catalogue;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.repository.CatalogueRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final ShopService shopService;
    private final ProductService productService;

    public Catalogue getCatalogue(String shopServiceName) {
        return catalogueRepository.findByShop_ServiceName(shopServiceName).orElseThrow(
                () -> new CatalogueException(HttpStatus.NOT_FOUND, "Catalogue for shop with name " + shopServiceName + " not found!")
        );
    }

    public CatalogueDto getCatalogueDto(String shopServiceName) {
        Catalogue catalogue = getCatalogue(shopServiceName);
        List<ProductDto> productDtoSet = productService.convertProductDtoListFromProducts(catalogue.getProducts());

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(productDtoSet)
                .shopServiceName(catalogue.getShop().getServiceName())
                .build();
    }

    public void saveCatalogueFromShop(CatalogueDto catalogueDto) {
        if (catalogueRepository.existsByShop_ServiceName(catalogueDto.getShopServiceName()))
            catalogueRepository.delete(getCatalogue(catalogueDto.getShopServiceName()));

        List<Product> products = productService.convertProductListFromDto(catalogueDto.getProducts());
        Catalogue catalogue = new Catalogue();

        catalogue.setCatalogueOnDate(catalogueDto.getCatalogueOnDate());
        catalogue.setProducts(products);
        catalogue.setShop(shopService.getShop(catalogueDto.getShopServiceName()));

        catalogueRepository.save(catalogue);
    }

}
