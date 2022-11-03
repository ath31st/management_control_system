package top.shop.shop1_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.shop.shop1_service.dto.CatalogueDto;
import top.shop.shop1_service.dto.ProductDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogueService {

    public CatalogueDto getCatalogue() {
        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setCatalogueOnDate(LocalDateTime.now());
        catalogueDto.setProducts(getListProductDtoFromBackend());
        return catalogueDto;
    }

    private List<ProductDto> getListProductDtoFromBackend() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:28889/api/products";
        ProductDto[] arrProducts = restTemplate.getForObject(url, ProductDto[].class);
        List<ProductDto> products = new ArrayList<>();
        if (arrProducts != null)
            products.addAll(List.of(arrProducts));
        return products;
    }

}
