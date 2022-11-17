package top.shop.shop1_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import top.shop.shop1_service.dto.product.ProductDto;

import java.time.LocalDateTime;
import java.util.Map;

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
}
