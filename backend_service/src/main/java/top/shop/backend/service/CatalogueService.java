package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.config.kafkaconfig.CatalogueProducer;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.entity.Catalogue;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.repository.CatalogueRepository;
import top.shop.backend.service.event.CatalogueEvent;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final CatalogueRepository catalogueRepository;
    private final ShopService shopService;
    private final ProductService productService;
    private final CatalogueProducer catalogueProducer;

    public Catalogue createCatalogue(CatalogueDto catalogueDto) {
        Catalogue catalogue = new Catalogue();
        catalogue.setCatalogueOnDate(LocalDateTime.now());
        catalogue.setProducts(productService.convertProductListFromDto(catalogueDto.getProducts()));
        catalogue.setShop(shopService.getShop(catalogueDto.getShop().getServiceName()));

        return catalogueRepository.save(catalogue);
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
