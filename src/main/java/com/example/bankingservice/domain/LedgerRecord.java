package com.example.bankingservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ledger_records")
public class LedgerRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "date_time")
    LocalDateTime dateTime;

    @Column(name = "amount")
    private Long amount;
}