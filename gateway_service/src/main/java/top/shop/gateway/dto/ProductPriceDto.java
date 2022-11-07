package top.shop.gateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductPriceDto {

    private String productName;
    private double price;

}
