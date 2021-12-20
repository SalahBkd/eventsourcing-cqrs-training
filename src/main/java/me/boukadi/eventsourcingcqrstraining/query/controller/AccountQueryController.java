package me.boukadi.eventsourcingcqrstraining.query.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boukadi.eventsourcingcqrstraining.commonapi.queries.GetAccountQuery;
import me.boukadi.eventsourcingcqrstraining.commonapi.queries.GetAllAccountsQuery;
import me.boukadi.eventsourcingcqrstraining.query.entity.Account;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.List;


@RestController
@RequestMapping("/query/accounts")
@AllArgsConstructor
@Slf4j
public class AccountQueryController {
    private QueryGateway queryGateway;

    @GetMapping("/allAccounts")
    public List<Account> getAccounts() {
        List<Account> response =
                queryGateway.query(
                        new GetAllAccountsQuery(),
                        ResponseTypes.multipleInstancesOf(Account.class)) // TODO: use DTOs
                        .join();
        return response;
    }

    @GetMapping("/byId/{id}")
    public Account getAccount(@PathVariable String id) {
       return queryGateway.query(new GetAccountQuery(id), ResponseTypes.instanceOf(Account.class)).join();
    }
}
