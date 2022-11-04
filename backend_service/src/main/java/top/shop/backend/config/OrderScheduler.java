package top.shop.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.shop.backend.entity.Order;
import top.shop.backend.service.OrderService;
import top.shop.backend.service.ProductService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class OrderScheduler {

    private final OrderService orderService;
    private final ProductService productService;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    void orderProcessing() {
        List<Order> unExecutedOrders = orderService.getUnexecutedOrders();

        if (!unExecutedOrders.isEmpty()) {
            unExecutedOrders.forEach(o -> {
                orderService.processingDelivery(o);
                log.info("order №{} is delivered", o.getId());

                productService.changeAmountProducts(o.getAmount(), o.getProductName());

                orderService.setExecutedStatusOrder(o);
                log.info("order №{} set \"executed\" status", o.getId());
            });

        }

    }
}
