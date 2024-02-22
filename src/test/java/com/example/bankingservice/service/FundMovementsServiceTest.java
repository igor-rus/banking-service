package com.example.bankingservice.service;

import com.example.bankingservice.domain.*;
import com.example.bankingservice.repository.LedgerRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.bankingservice.domain.OperationType.DEPOSIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FundMovementsServiceTest {
    @InjectMocks
    private FundMovementsService fundMovementsService;

    @Mock
    private LedgerRecordRepository ledgerRecordRepository;

    @Captor
    private ArgumentCaptor<List<LedgerRecord>> captor;

    @Test
    public void createLedgerRecord_AddsCorrectLedgerRecord() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setExternalId(UUID.randomUUID());
        transaction.setOperationType(DEPOSIT);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setAmount(new BigDecimal("100.50"));
        Account account = new Account();
        account.setAccountNumber("1234567890");

        TransactionType credit = TransactionType.Cr;
        fundMovementsService.createLedgerRecord(transaction, credit, account, 10050L);

        verify(ledgerRecordRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
        LedgerRecord ledgerRecord = captor.getValue().get(0);
        assertThat(ledgerRecord.getAccountNumber()).isEqualTo("1234567890");
        assertThat(ledgerRecord.getAmount()).isEqualTo(10050L);
        assertThat(ledgerRecord.getTransactionType()).isEqualTo(TransactionType.Cr);
        assertThat(ledgerRecord.getDateTime()).isNotNull();
    }
}