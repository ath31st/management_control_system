package top.shop.gateway.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ShopDto implements Serializable {
    @Size(min = 1, max = 100)
    private String name;
    private double balance;
}