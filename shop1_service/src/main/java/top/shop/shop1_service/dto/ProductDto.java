package top.shop.shop1_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ProductDto implements Serializable {
    @Size(min = 1, max = 100)
    private String name;
    private String category;
    private double price;
    private long amount;
}