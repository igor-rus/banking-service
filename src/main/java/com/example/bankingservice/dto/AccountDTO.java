package com.example.bankingservice.dto;

public record AccountDTO(Long externalId, String accountNumber, String type, String balance, String currency) {
}