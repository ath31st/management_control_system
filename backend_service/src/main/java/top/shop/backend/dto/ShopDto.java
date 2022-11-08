package top.shop.backend.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link top.shop.backend.entity.Shop} entity
 */
@Data
public class ShopDto implements Serializable {
    @Size(min = 1, max = 100)
    private String name;
    private String url;
    private double balance;
}