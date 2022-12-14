package top.shop.backend.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.CategoryDto;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.service.CategoryService;
import top.shop.backend.service.ShopService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final ShopService shopService;
    private final CategoryService categoryService;

    @Override
    public void run(String... args) {
        createDefaultShop();
        createDefaultCategory();
    }

    private void createDefaultShop() {
        if (shopService.shopExists("shop1")) return;

        ShopDto shop1 = new ShopDto();
        shop1.setServiceName("shop1");
        shop1.setName("DEFAULT");
        shop1.setUrl("http://localhost:28880");

        shopService.saveNewShop(shop1);
    }

    private void createDefaultCategory() {
        if (categoryService.categoryExists("default_category")) return;

        CategoryDto defaultCategory = new CategoryDto();
        defaultCategory.setServiceName("default_category");
        defaultCategory.setName("DEFAULT");
        defaultCategory.setDescription("This is default category");

        categoryService.saveCategory(defaultCategory);
    }

}