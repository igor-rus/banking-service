package com.example.bankingservice.controller;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.dto.TransferDTO;
import com.example.bankingservice.service.AccountService;
import com.example.bankingservice.service.BankOperationService;
import com.example.bankingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/transfers", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SecurityRequirement(name = "banking-service-api")
public class TransferController {
    private final BankOperationService bankOperationService;
    private final UserService userService;
    private final AccountService accountService;

    @PostMapping()
    @Operation(summary = "Deposit money to account", description = "Creates credit transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "bad request"),
            @ApiResponse(responseCode = "401", description = "unauthorized request"),
            @ApiResponse(responseCode = "403", description = "forbidden operation"),
            @ApiResponse(responseCode = "404", description = "resource not found"),
            @ApiResponse(responseCode = "500", description = "server error"),
    })
    public ResponseEntity<UUID> transfer(Principal principal, @Valid @RequestBody TransferDTO transfer)
    {
        User caller = userService.findByUsername(principal.getName());
        Account from = accountService.findAccountById(transfer.fromAccountId());
        Account to = accountService.findAccountById(transfer.toAccountId());
        UUID transactionId = bankOperationService.transfer(caller, from, to, transfer.amount());

        return ResponseEntity.ok().body(transactionId);
    }
}
