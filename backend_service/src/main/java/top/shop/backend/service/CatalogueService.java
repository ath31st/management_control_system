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
import top.shop.backend.entity.Catalogue;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.repository.CatalogueRepository;
import top.shop.backend.service.event.CatalogueEvent;

import java.time.LocalDateTime;
import java.util.Set;

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

    public Catalogue updateCatalogue(CatalogueDto catalogueDto) {
        Catalogue catalogue = getCatalogue(catalogueDto.getShopServiceName());
        catalogue.setProducts(productService.convertProductSetFromDto(catalogueDto.getProducts()));

        return catalogueRepository.save(catalogue);
    }

    public void catalogueHandler(CatalogueDto catalogueDto) {
        CatalogueDto catalogue;

        if (catalogueRepository.existsByShop_ServiceName(catalogueDto.getShopServiceName())) {
            updateCatalogue(catalogueDto);
        } else {
            createCatalogue(catalogueDto);
        }

        catalogue = getCatalogueDto(catalogueDto.getShopServiceName());
        eventPublisher.publishEvent(new CatalogueEvent(catalogue));
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
