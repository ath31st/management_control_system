package top.shop.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductServiceNameDto {
    private String shopServiceName;
    private List<String> addProductServiceNames;
    private List<String> deleteProductServiceNames;
}
