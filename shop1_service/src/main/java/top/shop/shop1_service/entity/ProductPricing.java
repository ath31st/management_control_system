package top.shop.shop1_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("prices")
public class ProductPricing {
    @Id
    private String productServiceName;
    private double price;
}
