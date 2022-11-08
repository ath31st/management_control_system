package top.shop.backend.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class BalanceEvent extends ApplicationEvent {
    public BalanceEvent(Object source) {
        super(source);
    }

    public BalanceEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
