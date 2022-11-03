package top.shop.shop1_service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CatalogueDto {

    private LocalDateTime catalogueOnDate;
    private List<ProductDto> products;

}
