package top.shop.backend.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class OrderEvent extends ApplicationEvent {
    public OrderEvent(Object source) {
        super(source);
    }

    public OrderEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
