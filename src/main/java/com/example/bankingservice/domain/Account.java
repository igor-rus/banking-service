package com.example.bankingservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Currency;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Data
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private Long externalId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "currency")
    private String currency;
}