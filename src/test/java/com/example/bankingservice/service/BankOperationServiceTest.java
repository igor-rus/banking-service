package com.example.bankingservice.service;

import com.example.bankingservice.domain.*;
import com.example.bankingservice.exception.OperationNotAllowedException;
import com.example.bankingservice.util.MoneyUtil;
import com.example.bankingservice.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.example.bankingservice.domain.OperationType.DEPOSIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankOperationServiceTest {
    @InjectMocks
    private BankOperationService bankOperationService;

    @Mock
    private AccountService accountService;

    @Mock
    private FundMovementsService fundMovementsService;

    @Mock
    private TransactionHistoryService transactionHistoryService;

    @Mock
    private ValidationService validationService;

    @Test
    void deposit_WhenValidationIsSuccessful_ShouldPerformDeposit() {
        UUID savedExternalId = UUID.randomUUID();
        User testUser = new User();
        Account testAccount = new Account();
        testAccount.setBalance(0L);
        BigDecimal depositAmount = new BigDecimal("10.00");

        when(validationService.validateDeposit(testUser, testAccount))
                .thenReturn(new ValidationResult(true, ""));

        Transaction newTransaction = new Transaction();
        newTransaction.setExternalId(savedExternalId);
        when(transactionHistoryService.createTransactionHistoryRecord(DEPOSIT, depositAmount)).thenReturn(newTransaction);

        doNothing().when(fundMovementsService).createLedgerRecord(newTransaction, TransactionType.Cr,
                testAccount, MoneyUtil.convertEurosToCents(depositAmount));
        doNothing().when(accountService).updateBalance(MoneyUtil.convertEurosToCents(depositAmount), testAccount);

        UUID result = bankOperationService.deposit(testUser, testAccount, depositAmount);
        assertThat(result).isEqualTo(savedExternalId);
    }

    @Test
    void deposit_WhenValidationFails_ShouldThrowException() {
        User testUser = new User();
        Account testAccount = new Account();

        when(validationService.validateDeposit(testUser, testAccount))
                .thenReturn(new ValidationResult(false, "Test error message"));

        assertThatThrownBy(() -> bankOperationService.deposit(testUser, testAccount, new BigDecimal("10.00")))
                .isInstanceOf(OperationNotAllowedException.class).hasMessage("Test error message");
    }

    @Test
    void testWithdraw_notValid() {
        User caller = new User();
        Account fromAccount = new Account();
        BigDecimal amount = new BigDecimal(10);
        ValidationResult validationResult = new ValidationResult(false, "Not enough balance");

        when(validationService.validateWithdrawal(caller, fromAccount, amount)).thenReturn(validationResult);

        assertThatThrownBy(() -> bankOperationService.withdraw(caller, fromAccount, amount))
                .isInstanceOf(OperationNotAllowedException.class)
                .hasMessage("Not enough balance");
    }


}