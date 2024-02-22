package com.example.bankingservice.controller;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.dto.AmountDTO;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/api/deposits",  consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SecurityRequirement(name = "banking-service-api")
public class DepositController {
    private final BankOperationService bankOperationService;
    private final UserService userService;
    private final AccountService accountService;

    @PostMapping("/{accountId}")
    @Operation(summary = "Deposit money to account", description = "Creates credit transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "401", description = "unauthorized request"),
            @ApiResponse(responseCode = "403", description = "operation not allowed"),
            @ApiResponse(responseCode = "404", description = "account not found"),
            @ApiResponse(responseCode = "500", description = "internal error")
    })
    public ResponseEntity<UUID> deposit(
            Principal principal,
            @PathVariable Long accountId,
            @Valid @RequestBody AmountDTO amountDTO)
    {
        User caller = userService.findByUsername(principal.getName());
        Account account = accountService.findAccountById(accountId);
        UUID transactionId = bankOperationService.deposit(caller, account, amountDTO.amount());

        return ResponseEntity.ok().body(transactionId);
    }
}
