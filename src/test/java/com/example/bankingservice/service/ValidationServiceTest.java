package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.AccountType;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.validation.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {
    ValidationService validationService = new ValidationService(
            new BigDecimal("5000.00"),
            new BigDecimal("500.00")
    );

    @Test
    void validateDepositToOwnAccount() {
        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setOwnerId(1L);

        ValidationResult validationResult = validationService.validateDeposit(user, account);

        assertThat(validationResult.isValid()).isTrue();
        assertThat(validationResult.message()).isEqualTo("");
    }

    @Test
    void testValidateDepositToOthersAccount() {
        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setOwnerId(2L);

        ValidationResult validationResult = validationService.validateDeposit(user, account);

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.message()).isEqualTo("You can only deposit to your own accounts");
    }

    @Test
    public void validateWithdrawalTest() {
        // Create data for test
        User user = new User();
        user.setId(123L);

        Account account = new Account();
        account.setOwnerId(123L);
        account.setAccountType(AccountType.REGULAR);
        account.setBalance(200L * 100);

        // Correct case
        ValidationResult validationResult = validationService.validateWithdrawal(user, account, new BigDecimal("100.50"));
        assertThat(validationResult.isValid()).isTrue();
        assertThat(validationResult.message()).isEqualTo("");

        // Incorrect - different user
        User differentUser = new User();
        differentUser.setId(456L);
        validationResult = validationService.validateWithdrawal(differentUser, account, new BigDecimal("100.25"));
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.message()).isEqualTo("You can only withdraw from your own accounts");

        // Incorrect - savings account
        Account savingsAccount = new Account();
        savingsAccount.setOwnerId(123L);
        savingsAccount.setAccountType(AccountType.SAVINGS);
        savingsAccount.setBalance(20000L);
        validationResult = validationService.validateWithdrawal(user, savingsAccount, new BigDecimal("100.01"));
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.message()).isEqualTo("Savings account not allowed for withdrawal");

        // Incorrect - insufficient funds
        validationResult = validationService.validateWithdrawal(user, account, new BigDecimal("300.00"));
        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.message()).isEqualTo("Insufficient funds for withdrawal");
    }

    @Test
    public void validateTransfer() {
        long hundredEurosInCents = 100L * 100;

        User user = new User();
        user.setId(1L);

        Account fromAccount = new Account();
        fromAccount.setOwnerId(1L);
        fromAccount.setAccountType(AccountType.REGULAR);
        fromAccount.setBalance(hundredEurosInCents);

        Account toAccount = new Account();
        toAccount.setOwnerId(2L);

        // Test: valid case
        ValidationResult validationResult = validationService.validateTransfer(user, fromAccount, toAccount, new BigDecimal("50.00"));
        assertThat(validationResult.isValid()).isTrue();

        // Test: not owner of the account
        user.setId(2L);
        validationResult = validationService.validateTransfer(user, fromAccount, toAccount,  new BigDecimal("50.00"));
        assertThat(validationResult.isValid()).isFalse();

        // Test: transfer from a savings account
        fromAccount.setAccountType(AccountType.SAVINGS);
        validationResult = validationService.validateTransfer(user, fromAccount, toAccount,  new BigDecimal("50.00"));
        assertThat(validationResult.isValid()).isFalse();

        // Test: internal transfer over limit
        user.setId(1L);
        fromAccount.setAccountType(AccountType.REGULAR);
        toAccount.setOwnerId(1L);
        validationResult = validationService.validateTransfer(user, fromAccount, toAccount,  new BigDecimal("5000.99"));
        assertThat(validationResult.isValid()).isFalse();

        // Test: external transfer over limit
        toAccount.setOwnerId(2L);
        validationResult = validationService.validateTransfer(user, fromAccount, toAccount, new BigDecimal("599.99"));
        assertThat(validationResult.isValid()).isFalse();
    }
}