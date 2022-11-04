package top.shop.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.shop.backend.service.CatalogueService;

@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class CatalogueScheduler {


    private final CatalogueService catalogueService;

    @Scheduled(fixedDelay = 10000)
    private void sendCatalogue() {
        catalogueService.sendCatalogue();
    }
}
