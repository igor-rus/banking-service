package com.example.bankingservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferDTO(
        @NotNull
        Long fromAccountId,
        @NotNull
        Long toAccountId,
        @NotNull
        @Positive(message = "Amount should be positive")
        @DecimalMin(value = "1.0", inclusive = false)
        @Digits(integer = 1000000, fraction = 2)
        BigDecimal amount
){}
