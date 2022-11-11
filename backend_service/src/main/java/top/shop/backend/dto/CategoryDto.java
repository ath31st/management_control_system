package top.shop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link top.shop.backend.entity.Category} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDto implements Serializable {

    private String name;
    private String serviceName;
    private String description;

}