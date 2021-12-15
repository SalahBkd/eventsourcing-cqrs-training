package me.boukadi.eventsourcingcqrstraining.commonapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateAccountRequestDTO {
    private double initialBalance;
    private String currency;
}
