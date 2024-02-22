package com.example.bankingservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AmountDTO(
        @NotNull
        @Positive(message = "Amount should be positive")
        @DecimalMin(value = "1.0", inclusive = false)
        @Digits(integer = 1000000, fraction = 2)
        BigDecimal amount
){}
