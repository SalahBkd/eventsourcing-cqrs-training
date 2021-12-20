package me.boukadi.eventsourcingcqrstraining.query.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.boukadi.eventsourcingcqrstraining.commonapi.enums.AccountStatus;

import javax.persistence.*;
import java.util.Collection;

@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class Account {
    @Id
    private String id;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private String currency;
    @OneToMany(mappedBy = "account")
    private Collection<Operation> operations;
}
