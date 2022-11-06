package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.entity.Product;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.config.kafkaconfig.CatalogueProducer;
import top.shop.backend.service.event.CatalogueEvent;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final ProductService productService;
    private final CatalogueProducer catalogueProducer;

    public CatalogueDto createCatalogue() {
        CatalogueDto catalogue = new CatalogueDto();
        catalogue.setCatalogueOnDate(LocalDateTime.now());
        catalogue.setProducts(productService.getListProductDto());
        return catalogue;
    }

    @EventListener
    public void sendCatalogue(CatalogueEvent event) {
        if ((boolean) event.getSource())
            try {
                catalogueProducer.sendMessage(createCatalogue());
            } catch (JsonProcessingException e) {
                throw new CatalogueException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
    }
}
