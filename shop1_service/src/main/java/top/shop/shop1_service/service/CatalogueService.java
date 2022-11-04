package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.shop.shop1_service.dto.CatalogueDto;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    //TODO MAKE STORAGE FOR CATALOGUE
    public static CatalogueDto catalogue;

    public CatalogueDto getCatalogue() {
        if (catalogue != null)
            return catalogue;
        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setCatalogueOnDate(LocalDateTime.now());
        catalogueDto.setProducts(Collections.emptyList());
        return catalogueDto;
    }
}
