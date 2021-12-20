package me.boukadi.eventsourcingcqrstraining.commonapi.commands;

import lombok.Getter;

import java.util.Date;

public class CreditAccountCommand extends BaseCommand<String>{
    @Getter private double amount;
    @Getter private String currency;
    @Getter private Date date;
    public CreditAccountCommand(String id, double amount, String currency, Date date) {
        super(id);
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }
}
