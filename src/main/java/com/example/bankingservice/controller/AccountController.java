package com.example.bankingservice.controller;

import com.example.bankingservice.dto.AccountDTO;
import com.example.bankingservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/api/accounts", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@SecurityRequirement(name = "banking-service-api")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @Operation(
            summary = "Fetch all user accounts",
            description = "fetches all user's accounts and their balances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "401", description = "unauthorized request"),
            @ApiResponse(responseCode = "403", description = "operation not allowed"),
            @ApiResponse(responseCode = "500", description = "internal error")
    })
    public ResponseEntity<List<AccountDTO>> accounts(Principal principal) {
        List<AccountDTO> accounts = accountService.accounts(principal);

        return ResponseEntity.ok().body(accounts);
    }


    @GetMapping("/{accountId}")
    @Operation(
            summary = "Fetch one account ",
            description = "fetches specific account information and balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "401", description = "unauthorized request"),
            @ApiResponse(responseCode = "403", description = "operation not allowed"),
            @ApiResponse(responseCode = "404", description = "account not found"),
            @ApiResponse(responseCode = "500", description = "internal error")
    })
    public ResponseEntity<AccountDTO> accountDetails(Principal principal, @PathVariable Long accountId) {
        return accountService.accountDetails(principal, accountId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
