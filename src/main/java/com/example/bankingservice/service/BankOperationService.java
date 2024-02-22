package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.Transaction;
import com.example.bankingservice.domain.TransactionType;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.exception.OperationNotAllowedException;
import com.example.bankingservice.util.MoneyUtil;
import com.example.bankingservice.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.bankingservice.domain.OperationType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BankOperationService {
    private final AccountService accountService;
    private final FundMovementsService fundMovementsService;
    private final TransactionHistoryService transactionHistoryService;
    private final ValidationService validationService;

    public UUID deposit(User caller, Account to, BigDecimal amount) {
        long cents = MoneyUtil.convertEurosToCents(amount);

        ValidationResult validationResult = validationService.validateDeposit(caller, to);
        if (validationResult.isValid()) {
            Transaction saved = transactionHistoryService.createTransactionHistoryRecord(DEPOSIT, amount);
            fundMovementsService.createLedgerRecord(saved, TransactionType.Cr, to, cents);
            accountService.updateBalance(to.getBalance() + cents, to);

            return saved.getExternalId();
        } else {
            throw new OperationNotAllowedException(validationResult.message());
        }
    }

    public UUID withdraw(User caller, Account from, BigDecimal amount) {
        long cents = MoneyUtil.convertEurosToCents(amount);

        ValidationResult validationResult = validationService.validateWithdrawal(caller, from, amount);
        if (validationResult.isValid()) {
            Transaction historyRecord = transactionHistoryService.createTransactionHistoryRecord(WITHDRAWAL, amount);
            fundMovementsService.createLedgerRecord(historyRecord, TransactionType.Db, from, cents);
            accountService.updateBalance(from.getBalance() - cents, from);

            return historyRecord.getExternalId();
        } else {
            throw new OperationNotAllowedException(validationResult.message());
        }
    }

    public UUID transfer(User caller, Account from, Account to, BigDecimal amount) {
        long cents = MoneyUtil.convertEurosToCents(amount);
        ValidationResult validationResult = validationService.validateTransfer(caller, from, to, amount);
        if (validationResult.isValid()) {
            Transaction historyRecord = transactionHistoryService.createTransactionHistoryRecord(TRANSFER, amount);
            fundMovementsService.createLedgerRecord(historyRecord, TransactionType.Db, from, cents);
            fundMovementsService.createLedgerRecord(historyRecord, TransactionType.Cr, to, cents);
            accountService.updateBalance(from.getBalance() - cents, from);
            accountService.updateBalance(to.getBalance() + cents, to);

            return historyRecord.getExternalId();
        } else {
            throw new OperationNotAllowedException(validationResult.message());
        }
    }
}
