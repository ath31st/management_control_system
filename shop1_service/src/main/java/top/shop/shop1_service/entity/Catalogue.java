package top.shop.shop1_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import top.shop.shop1_service.dto.product.ProductDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("catalogs")
public class Catalogue {
    @Id
    private String shopServiceName;
    private LocalDateTime catalogueOnDate;
    private Map<String, ProductDto> products;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catalogue catalogue = (Catalogue) o;
        return Objects.equals(shopServiceName, catalogue.shopServiceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shopServiceName);
    }
}
