package me.boukadi.eventsourcingcqrstraining.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boukadi.eventsourcingcqrstraining.commonapi.enums.OperationType;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountActivatedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountCreatedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountCreditedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountDebitedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.queries.GetAccountQuery;
import me.boukadi.eventsourcingcqrstraining.commonapi.queries.GetAllAccountsQuery;
import me.boukadi.eventsourcingcqrstraining.query.entity.Account;
import me.boukadi.eventsourcingcqrstraining.query.entity.Operation;
import me.boukadi.eventsourcingcqrstraining.query.repo.AccountRepo;
import me.boukadi.eventsourcingcqrstraining.query.repo.OperationRepo;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor // for DI
@Slf4j
// with this pattern we are not handling CRUD operations based on the controller but with EVENTS
public class AccountServiceHandler {
    private AccountRepo accountRepo;
    private OperationRepo operationRepo;

    @EventHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        // we are not handling business logic here, it's dealt with by the aggregate

        log.info("========================");
        log.info("AccountCreatedEvent received");
        // if we have non persistent data on event store they will be executed to persist them
        // IMPORTANT: remember that COMMAND and QUERY are seperated aspects, we can run just the command part
        // and when we need to persist data we can run the command part and all the data that are in wait in
        // the store will be persisted
        Account account = new Account();
        account.setId(accountCreatedEvent.getId());
        account.setBalance(accountCreatedEvent.getInitialBalance());
        account.setAccountStatus(accountCreatedEvent.getAccountStatus());
        account.setCurrency(accountCreatedEvent.getCurrency());
        account.setOperations(null);
        accountRepo.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent accountActivatedEvent) {
        log.info("========================");
        log.info("AccountActivatedEvent received");
        Account account = accountRepo.findById(accountActivatedEvent.getId()).get();
        account.setAccountStatus(accountActivatedEvent.getStatus());
        accountRepo.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent accountCreditedEvent) {
        log.info("========================");
        log.info("AccountCreditedEvent received");
        Account account = accountRepo.findById(accountCreditedEvent.getId()).get();
        Operation operation = new Operation();

        operation.setAmount(accountCreditedEvent.getAmount());
//        operation.setDate(new Date()); // AVOID THIS, the date must the one of the event
        operation.setDate(accountCreditedEvent.getDate());
        operation.setOperationType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepo.save(operation);

        account.setBalance(account.getBalance() + accountCreditedEvent.getAmount());
        accountRepo.save(account);
    }

    @EventHandler
    public void on(AccountDebitedEvent accountDebitedEvent) {
        log.info("========================");
        log.info("AccountDebitedEvent received");
        Account account = accountRepo.findById(accountDebitedEvent.getId()).get();
        Operation operation = new Operation();

        operation.setAmount(accountDebitedEvent.getAmount());
//        operation.setDate(new Date()); // AVOID THIS, the date must the one of the event
        operation.setDate(accountDebitedEvent.getDate());
        operation.setOperationType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepo.save(operation);

        account.setBalance(account.getBalance() - accountDebitedEvent.getAmount());
        accountRepo.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery accountsQuery) {
       return accountRepo.findAll();
    }

    @QueryHandler
    public Account on(GetAccountQuery getAccountQuery) {
        return accountRepo.findById(getAccountQuery.getId()).get();
    }
}
