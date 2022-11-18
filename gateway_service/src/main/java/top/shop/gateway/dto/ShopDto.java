package top.shop.gateway.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ShopDto implements Serializable {
    @NotEmpty(message = "Name cannot be empty.")
    @Size(min = 3, max = 100, message = "Name must be minimum 3 and maximum 100 characters.")
    private String name;
    @NotEmpty(message = "URL cannot be empty.")
    @Size(min = 3, max = 100, message = "URL must be minimum 3 and maximum 100 characters.")
    private String url;
    @NotEmpty(message = "Service name cannot be empty.")
    @Size(min = 3, max = 100, message = "Service name must be minimum 3 and maximum 100 characters.")
    private String serviceName;
    private double balance;
}