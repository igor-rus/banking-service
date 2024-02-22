package com.example.bankingservice.repository;

import com.example.bankingservice.domain.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends CrudRepository<Transaction, Long> {
    Optional<Transaction> findTransactionByExternalId(UUID externalId);
}
