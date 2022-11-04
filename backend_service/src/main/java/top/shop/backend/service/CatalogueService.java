package top.shop.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import top.shop.backend.dto.CatalogueDto;
import top.shop.backend.exceptionhandler.exception.CatalogueException;
import top.shop.backend.service.kafkalogic.CatalogueProducer;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CatalogueService {

    private final ProductService productService;
    private final CatalogueProducer catalogueProducer;

    public CatalogueDto getCatalogue() {
        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setCatalogueOnDate(LocalDateTime.now());
        catalogueDto.setProducts(productService.getListProductDto());
        return catalogueDto;
    }

    public void sendCatalogue() {

        try {
            catalogueProducer.sendMessage(getCatalogue());
        } catch (JsonProcessingException e) {
            throw new CatalogueException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
