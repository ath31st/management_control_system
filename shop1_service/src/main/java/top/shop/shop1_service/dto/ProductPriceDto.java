package top.shop.shop1_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link top.shop.shop1_service.entity.ProductPrice} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPriceDto implements Serializable {
    private String productName;
    private double price;
}