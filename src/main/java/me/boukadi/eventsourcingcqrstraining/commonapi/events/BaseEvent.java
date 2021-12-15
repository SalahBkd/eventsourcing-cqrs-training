package me.boukadi.eventsourcingcqrstraining.commonapi.events;

import lombok.Getter;

public abstract class BaseEvent<T> {
    // No @TargetAggregateIdentifier like base command, it's a simple object
    @Getter private T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}
