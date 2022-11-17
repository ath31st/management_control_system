package top.shop.backend.dto;

import lombok.*;
import top.shop.backend.dto.product.ProductDto;
import top.shop.backend.entity.Catalogue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link Catalogue} entity
 */
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