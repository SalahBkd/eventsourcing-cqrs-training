package me.boukadi.eventsourcingcqrstraining.commonapi.dto;

import lombok.Data;

@Data
public class DebitAccountRequestDTO {
    private String accountID;
    private double amount;
    private String currency;
}
