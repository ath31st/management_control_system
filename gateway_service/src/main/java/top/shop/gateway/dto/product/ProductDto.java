package top.shop.gateway.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.shop.gateway.dto.CategoryDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto implements Serializable {
    @NotEmpty(message = "Name cannot be empty.")
    @Size(min = 3, max = 100, message = "Name must be minimum 3 and maximum 100 characters.")
    private String name;
    @NotEmpty(message = "Service name cannot be empty.")
    @Size(min = 3, max = 100, message = "Service name must be minimum 3 and maximum 100 characters.")
    private String serviceName;
    @NotEmpty(message = "Description cannot be empty.")
    @Size(min = 3, max = 300, message = "Description must be minimum 3 and maximum 300 characters.")
    private String description;
    @Min(value = 0, message = "Price cannot be lower 0")
    @Max(value = 100000, message = "Price cannot be higher 100000")
    private double price;
    @Min(value = 0, message = "Amount cannot be lower 0")
    @Max(value = 100000, message = "Amount cannot be higher 100000")
    private long amount;
    private CategoryDto category;
}