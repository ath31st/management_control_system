package top.shop.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.CatalogueProducer;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.entity.Catalogue;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.repository.CatalogueRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final ShopService shopService;
    private final ProductService productService;
    private final CatalogueProducer catalogueProducer;

    public Catalogue getCatalogue(String shopServiceName) {
        return catalogueRepository.findByShop_ServiceName(shopServiceName).orElseThrow(
                () -> new CatalogueException(HttpStatus.NOT_FOUND, "Catalogue for shop with name " + shopServiceName + " not found!")
        );
    }

    public CatalogueDto getCatalogueDto(String shopServiceName) {
        Catalogue catalogue = getCatalogue(shopServiceName);

        return CatalogueDto.builder()
                .catalogueOnDate(LocalDateTime.now())
                .products(productService.convertProductDtoSetFromProducts(catalogue.getProducts()))
                .shop(shopService.getShopDto(shopServiceName))
                .build();
    }

    public Catalogue createCatalogue(CatalogueDto catalogueDto) {
        Catalogue catalogue = new Catalogue();
        catalogue.setCatalogueOnDate(LocalDateTime.now());
        catalogue.setProducts(productService.convertProductSetFromDto(catalogueDto.getProducts()));
        catalogue.setShop(shopService.getShop(catalogueDto.getShop().getServiceName()));

        return catalogueRepository.save(catalogue);
    }

    public Catalogue updateCatalogue(CatalogueDto catalogueDto) {
        Catalogue catalogue = getCatalogue(catalogueDto.getShop().getServiceName());
        catalogue.setProducts(productService.convertProductSetFromDto(catalogueDto.getProducts()));

        return catalogueRepository.save(catalogue);
    }

    public CatalogueDto catalogueHandler(CatalogueDto catalogueDto) {
        if (catalogueRepository.existsByShop_ServiceName(catalogueDto.getShop().getServiceName())) {
            updateCatalogue(catalogueDto);
        } else {
            createCatalogue(catalogueDto);
        }
        return getCatalogueDto(catalogueDto.getShop().getServiceName());
    }

//    @EventListener
//    public void sendCatalogue(CatalogueEvent event) {
//        if ((boolean) event.getSource())
//            try {
//                catalogueProducer.sendMessage(createCatalogue());
//            } catch (JsonProcessingException e) {
//                throw new CatalogueException(HttpStatus.BAD_REQUEST, e.getMessage());
//            }
//    }
}
