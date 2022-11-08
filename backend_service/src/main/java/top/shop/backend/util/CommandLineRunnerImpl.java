package top.shop.backend.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.shop.backend.dto.ShopDto;
import top.shop.backend.service.ShopService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final ShopService shopService;

    @Override
    public void run(String... args) {
        ShopDto shop1 = new ShopDto();
        shop1.setName("shop1");
        shop1.setUrl("http://localhost:28880");

        shopService.saveNewShop(shop1);
        log.info("shop with name {} added!", shop1.getName());
    }

}