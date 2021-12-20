package me.boukadi.eventsourcingcqrstraining.commands.aggregate;

import me.boukadi.eventsourcingcqrstraining.commonapi.commands.CreateAccountCommand;
import me.boukadi.eventsourcingcqrstraining.commonapi.commands.CreditAccountCommand;
import me.boukadi.eventsourcingcqrstraining.commonapi.commands.DebitAccountCommand;
import me.boukadi.eventsourcingcqrstraining.commonapi.enums.AccountStatus;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountActivatedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountCreatedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountCreditedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.events.AccountDebitedEvent;
import me.boukadi.eventsourcingcqrstraining.commonapi.exception.InsufficientBalanceException;
import me.boukadi.eventsourcingcqrstraining.commonapi.exception.NegativeAmountException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    // this identifier is linked to the one in commonapi/commands/BaseCommand
    @AggregateIdentifier
    private String accountID;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // REQUIRED BY AXON
    }

    // I'm listening to the EventBus, When i receive CreateAccountCommand Axon instantiates this Aggregate
    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        // Business Logic
        if(createAccountCommand.getInitialBalance() < 0) throw new RuntimeException("Balance must be greater than 0");
        // if conditions are true
        AggregateLifecycle.apply(new AccountCreatedEvent(
                // we transfer Command data to the Event, and Axon will take care of persisting data in Event Store because he receives an Event
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency()
        ));
    }

    // Evolution Function, when AccountCreatedEvent is emitted this handler executes, doesn't deal with business logic, it changes the app state
    // the app state is the Aggregate
    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountID = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;

        // after the state is changed i can also emmit new Events
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));

    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }

    // CREDIT ACCOUNT
    @CommandHandler // when the command is emitted in the command bus this method gets executed
    public void handle(CreditAccountCommand creditAccountCommand) {
        if(creditAccountCommand.getAmount() < 0) throw new NegativeAmountException("amount should not be lesser than 0");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent accountCreditedEvent) {
        this.balance += accountCreditedEvent.getAmount();
    }


    // DEBIT ACCOUNT
    @CommandHandler // when the command is emitted in the command bus this method gets executed
    public void handle(DebitAccountCommand debitAccountCommand) {
        if(debitAccountCommand.getAmount() < 0) throw new NegativeAmountException("Amount should not be lesser than 0");
        if(this.balance < debitAccountCommand.getAmount()) throw new InsufficientBalanceException("Insufficient Balance: " + balance + ", the amount is greater than the current account balance.");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent accountDebitedEvent) {
        this.balance -= accountDebitedEvent.getAmount();
    }



}
