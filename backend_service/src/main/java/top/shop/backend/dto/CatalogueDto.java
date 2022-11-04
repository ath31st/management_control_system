package top.shop.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CatalogueDto {

    private LocalDateTime catalogueOnDate;
    private List<ProductDto> products;

}
