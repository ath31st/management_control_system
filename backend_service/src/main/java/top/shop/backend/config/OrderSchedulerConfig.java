package top.shop.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.shop.backend.entity.Order;
import top.shop.backend.service.OrderService;
import top.shop.backend.util.OrderStatus;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArraySet;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
@RequiredArgsConstructor
public class OrderSchedulerConfig {
    private final OrderService orderService;
    private static final CopyOnWriteArraySet<Order> orders = new CopyOnWriteArraySet<>();

    @PostConstruct
    public void init() {
        orders.addAll(orderService.getOrdersByStatus(OrderStatus.CREATED));
    }

    @Scheduled(fixedDelay = 5000)
    private void checkAndUpdateOrderStatus() {
        orders.stream()
                .filter(this::checkIsExpiredOrder)
                .forEach(o -> {
                    o.setStatus(OrderStatus.EXPIRED);
                    o.setExecutionDate(LocalDateTime.now());
//                    Payment p = o.getPayment();
//                    if (p.getPaymentStatus().equals(PaymentStatus.UNPAID))
//                        p.setPaymentStatus(PaymentStatus.EXPIRED);
//                    o.setPayment(p);
                    orderService.saveOrderChanges(o);
                });
    }

    private boolean checkIsExpiredOrder(Order order) {
        return LocalDateTime.now().isAfter(order.getOrderDate().plusMinutes(order.getPayment().getMinutesBeforeExpiration()));
    }

}
