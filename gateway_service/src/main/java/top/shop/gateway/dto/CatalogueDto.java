package top.shop.gateway.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CatalogueDto {

    private LocalDateTime catalogueOnDate;
    private List<ProductDto> products;

}
