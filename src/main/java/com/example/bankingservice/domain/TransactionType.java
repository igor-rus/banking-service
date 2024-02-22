package com.example.bankingservice.domain;

public enum TransactionType {
    Db("Debit"),
    Cr("Credit");

    final String description;

    TransactionType(String description){
        this.description = description;
    }
}
