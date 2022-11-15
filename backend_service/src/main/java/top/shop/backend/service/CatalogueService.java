package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.CatalogueProducer;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.dto.ProductDto;
import top.shop.backend.dto.ProductServiceNameDto;
import top.shop.backend.entity.Catalogue;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.repository.CatalogueRepository;
import top.shop.backend.service.event.CatalogueEvent;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final ShopService shopService;
    private final ProductService productService;
    private final CatalogueProducer catalogueProducer;
    private final ApplicationEventPublisher eventPublisher;

    public Catalogue getCatalogue(String shopServiceName) {
        return catalogueRepository.findByShop_ServiceName(shopServiceName).orElseThrow(
                () -> new CatalogueException(HttpStatus.NOT_FOUND, "Catalogue for shop with name " + shopServiceName + " not found!")
        );
    }

    public CatalogueDto getCatalogueDto(String shopServiceName) {
        Catalogue catalogue = getCatalogue(shopServiceName);
        Set<ProductDto> productDtoSet = productService.convertProductDtoSetFromProducts(catalogue.getProducts());

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(productDtoSet)
                .shopServiceName(catalogue.getShop().getServiceName())
                .build();
    }

    public Catalogue createCatalogue(CatalogueDto catalogueDto) {
        Catalogue catalogue = new Catalogue();
        catalogue.setCatalogueOnDate(LocalDateTime.now());
        catalogue.setProducts(productService.convertProductSetFromDto(catalogueDto.getProducts()));
        catalogue.setShop(shopService.getShop(catalogueDto.getShopServiceName()));

        return catalogueRepository.save(catalogue);
    }

    public Catalogue updateCatalogue(ProductServiceNameDto productServiceNameDto) {
        Catalogue catalogue = getCatalogue(productServiceNameDto.getShopServiceName());

        Set<Product> products = catalogue.getProducts()
                .stream()
                .filter(p -> !productServiceNameDto.getDeleteProductServiceNames().contains(p.getServiceName()))
                .collect(Collectors.toSet());
        products.addAll(productService.getProductsByListServiceNames(productServiceNameDto.getAddProductServiceNames()));

        catalogue.setProducts(products);

        CatalogueDto catalogueDto = getCatalogueDto(productServiceNameDto.getShopServiceName());
        eventPublisher.publishEvent(new CatalogueEvent(catalogueDto));

        return catalogueRepository.save(catalogue);
    }

    public void catalogueHandler(CatalogueDto catalogueDto) {
        if (!catalogueRepository.existsByShop_ServiceName(catalogueDto.getShopServiceName())) {
            createCatalogue(catalogueDto);

            CatalogueDto catalogue = getCatalogueDto(catalogueDto.getShopServiceName());
            eventPublisher.publishEvent(new CatalogueEvent(catalogue));
        }
    }

    @EventListener
    public void sendCatalogue(CatalogueEvent event) {
        try {
            catalogueProducer.sendMessage((CatalogueDto) event.getSource());
        } catch (JsonProcessingException e) {
            throw new CatalogueException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
