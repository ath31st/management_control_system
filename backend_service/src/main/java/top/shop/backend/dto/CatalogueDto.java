package top.shop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class CatalogueDto implements Serializable {
    private LocalDateTime catalogueOnDate;
    private ShopDto shop;
    private Set<ProductDto> products;
}