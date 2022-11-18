package top.shop.gateway.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPricingDto implements Serializable {
    private String productServiceName;
    @Min(value = 0, message = "Price cannot be lower 0")
    @Max(value = 100000, message = "Price cannot be higher 100000")
    private double price;
}