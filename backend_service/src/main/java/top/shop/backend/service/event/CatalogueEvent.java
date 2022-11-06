package top.shop.backend.service.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class CatalogueEvent extends ApplicationEvent {

    public CatalogueEvent(Object source) {
        super(source);
    }

    public CatalogueEvent(Object source, Clock clock) {
        super(source, clock);
    }

}
