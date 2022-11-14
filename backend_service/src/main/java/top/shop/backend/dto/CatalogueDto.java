package top.shop.backend.dto;

import lombok.*;
import top.shop.backend.entity.Catalogue;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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
    private Set<ProductDto> products;
}