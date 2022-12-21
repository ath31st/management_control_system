package top.shop.shop1_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPricing that = (ProductPricing) o;
        return Objects.equals(productServiceName, that.productServiceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productServiceName);
    }
}
