package top.shop.backend.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.backend.entity.Shop;
import top.shop.backend.service.ShopService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final ShopService shopService;

    @Override
    public void run(String... args) {
        Shop shop1 = new Shop();
        shop1.setName("shop1");

        Shop shop2 = new Shop();
        shop2.setName("shop2");

        Shop shop3 = new Shop();
        shop3.setName("shop3");

        shopService.saveNewShop(shop1);
        log.info("shop with name {} added!", shop1.getName());
        shopService.saveNewShop(shop2);
        log.info("shop with name {} added!", shop2.getName());
        shopService.saveNewShop(shop3);
        log.info("shop with name {} added!", shop3.getName());
    }
}