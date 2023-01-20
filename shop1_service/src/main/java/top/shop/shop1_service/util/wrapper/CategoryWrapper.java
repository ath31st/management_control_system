package top.shop.shop1_service.util.wrapper;

import lombok.Data;
import top.shop.shop1_service.dto.CategoryDto;

@Data
public class CategoryWrapper {
    private CategoryDto updatedCategory;
    private CategoryDto replacementCategory;
    private String deletedCategory;
}
