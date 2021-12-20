package me.boukadi.eventsourcingcqrstraining.commonapi.dto;

import lombok.Data;

@Data
public class CreditAccountRequestDTO {
    private String accountID;
    private double amount;
    private String currency;
}
