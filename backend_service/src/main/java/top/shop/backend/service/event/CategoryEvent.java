package top.shop.backend.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class CategoryEvent extends ApplicationEvent {

    public CategoryEvent(Object source) {
        super(source);
    }

    public CategoryEvent(Object source, Clock clock) {
        super(source, clock);
    }

}
