package me.boukadi.eventsourcingcqrstraining.commands.controller;

import lombok.AllArgsConstructor;
import me.boukadi.eventsourcingcqrstraining.commonapi.commands.CreateAccountCommand;
import me.boukadi.eventsourcingcqrstraining.commonapi.dto.CreateAccountRequestDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping(path = "/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO requestDTO) {
        CompletableFuture<String> commandResponse = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                requestDTO.getInitialBalance(),
                requestDTO.getCurrency()
        ));
        return commandResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception) {
        ResponseEntity<String> entity = new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return entity;
    }

    @GetMapping(path = "/eventStore/{accountID}")
    public Stream eventStore(@PathVariable String accountID) {
        return eventStore.readEvents(accountID).asStream();
    }

}
