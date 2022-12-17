package top.shop.shop1_service.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class PaymentEvent extends ApplicationEvent {

    public PaymentEvent(Object source) {
        super(source);
    }

    public PaymentEvent(Object source, Clock clock) {
        super(source, clock);
    }

}
