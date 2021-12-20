package me.boukadi.eventsourcingcqrstraining.commonapi.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException(String msg) {
        super(msg);
    }
}
