package top.shop.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto implements Serializable {
    @NotEmpty(message = "Name cannot be empty.")
    @Size(min = 3, max = 100, message = "Name must be minimum 3 and maximum 100 characters.")
    private String name;
    @NotEmpty(message = "Service name cannot be empty.")
    @Size(min = 3, max = 100, message = "Service name must be minimum 3 and maximum 100 characters.")
    private String serviceName;
    @NotEmpty(message = "Description cannot be empty.")
    @Size(min = 3, max = 300, message = "Description must be minimum 3 and maximum 300 characters.")
    private String description;
}