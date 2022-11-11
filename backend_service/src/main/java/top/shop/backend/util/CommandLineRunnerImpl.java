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
        ShopDto shop1 = new ShopDto();
        shop1.setServiceName("shop1");
        shop1.setName("DEFAULT");
        shop1.setUrl("http://localhost:28880");

        shopService.saveNewShop(shop1);
        log.info("shop with name {} added!", shop1.getName());

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setServiceName("first_category");
        categoryDto1.setName("First category");
        categoryDto1.setDescription("This is first category");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setServiceName("second_category");
        categoryDto2.setName("Second category");
        categoryDto2.setDescription("This is second category");

        categoryService.saveCategory(categoryDto1);
        categoryService.saveCategory(categoryDto2);
    }

}