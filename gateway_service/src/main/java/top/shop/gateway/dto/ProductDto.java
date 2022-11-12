package top.shop.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto implements Serializable {
    @Size(min = 1, max = 100)
    private String name;
    private String serviceName;
    private String description;
    private double price;
    private long amount;
    private CategoryDto category;
}