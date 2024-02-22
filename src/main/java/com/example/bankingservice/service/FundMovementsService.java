package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.LedgerRecord;
import com.example.bankingservice.domain.Transaction;
import com.example.bankingservice.domain.TransactionType;
import com.example.bankingservice.repository.LedgerRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundMovementsService {
    private final LedgerRecordRepository ledgerRecordRepository;

    public void createLedgerRecord(Transaction saved, TransactionType transactionType, Account account, long amount) {

        LedgerRecord ledgerRecord = new LedgerRecord();
        ledgerRecord.setTransactionId(saved.getId());
        ledgerRecord.setTransactionType(transactionType);
        ledgerRecord.setDateTime(LocalDateTime.now());
        ledgerRecord.setAccountNumber(account.getAccountNumber());
        ledgerRecord.setAmount(amount);

        recordFundMovement(List.of(ledgerRecord));
    }

    private void recordFundMovement(List<LedgerRecord> ledgerRecords) {
        ledgerRecordRepository.saveAll(ledgerRecords);
    }
}
