package top.shop.backend.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class ProductAmountEvent extends ApplicationEvent {
    public ProductAmountEvent(Object source) {
        super(source);
    }

    public ProductAmountEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
