package top.shop.backend.util.wrapper;

import lombok.Data;
import top.shop.backend.dto.CategoryDto;

@Data
public class CategoryWrapper {
    private CategoryDto updatedCategory;
    private CategoryDto replacementCategory;
    private String deletedCategory;
}
