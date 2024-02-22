package com.example.bankingservice.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id")
    private UUID externalId;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    OperationType operationType;

    @Column(name = "date_time")
    LocalDateTime dateTime;

    @Column(name = "amount")
    BigDecimal amount;
}
