package top.shop.shop1_service.dto;

import lombok.*;
import top.shop.shop1_service.dto.product.ProductDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CatalogueDto implements Serializable {
    private LocalDateTime catalogueOnDate;
    private String shopServiceName;
    private List<ProductDto> products;
}
