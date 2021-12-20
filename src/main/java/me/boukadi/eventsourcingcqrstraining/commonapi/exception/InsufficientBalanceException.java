package me.boukadi.eventsourcingcqrstraining.commonapi.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String msg) {
        super(msg);
    }
}
