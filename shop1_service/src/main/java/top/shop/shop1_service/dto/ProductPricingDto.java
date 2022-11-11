package top.shop.shop1_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.shop1_service.entity.ProductPricing;

import java.io.Serializable;

/**
 * A DTO for the {@link ProductPricing} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPricingDto implements Serializable {
    private String productName;
    private double price;
}