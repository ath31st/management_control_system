package top.shop.gateway.dto;

import lombok.*;
import top.shop.gateway.dto.product.ProductDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
