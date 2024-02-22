package com.example.bankingservice.service;

import com.example.bankingservice.domain.OperationType;
import com.example.bankingservice.domain.Transaction;
import com.example.bankingservice.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;

    public Transaction createTransactionHistoryRecord(OperationType operationType, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setExternalId(UUID.randomUUID());
        transaction.setOperationType(operationType);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setAmount(amount);

        return transactionHistoryRepository.save(transaction);
    }

}
