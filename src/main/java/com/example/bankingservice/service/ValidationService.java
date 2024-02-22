package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.AccountType;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.util.MoneyUtil;
import com.example.bankingservice.validation.ValidationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ValidationService {
    private final BigDecimal internalTransferLimit;
    private final BigDecimal externalTransferLimit;

    public ValidationService(
            @Value("${bank.internalTransferLimit}") BigDecimal internalTransferLimit,
            @Value("${bank.externalTransferLimit}") BigDecimal externalTransferLimit
    ) {
        this.internalTransferLimit = internalTransferLimit;
        this.externalTransferLimit = externalTransferLimit;
    }
    private static final String OWN_ACCOUNT_DEPOSIT_MESSAGE = "You can only deposit to your own accounts";
    private static final String OWN_ACCOUNT_WITHDRAWAL_MESSAGE = "You can only withdraw from your own accounts";
    private static final String OWN_ACCOUNT_TRANSFER_MESSAGE = "You can only transfer from your own accounts";
    private static final String SAVINGS_ACCOUNT_WITHDRAWAL_MESSAGE = "Savings account not allowed for withdrawal";
    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds for withdrawal";
    private static final String INTERNAL_TRANSFER_OVER_LIMIT_MESSAGE = "Internal transfer exceeded limit";
    private static final String EXTERNAL_TRANSFER_OVER_LIMIT_MESSAGE = "External transfer exceeded limit";
    private static final String SAVINGS_ACCOUNT_TRANSFER_MESSAGE = "You cannot make a transfer from a savings account";

    public ValidationResult validateDeposit(User caller, Account to) {
        if (isOwnerOfAccount(caller, to)) {
            return new ValidationResult(true, "");
        } else {
            return new ValidationResult(false, OWN_ACCOUNT_DEPOSIT_MESSAGE);
        }
    }

    public ValidationResult validateWithdrawal(User caller, Account fromAccount, BigDecimal amount) {
        if (!isOwnerOfAccount(caller, fromAccount)) {
            return createValidationResult(false, OWN_ACCOUNT_WITHDRAWAL_MESSAGE);
        }
        if (fromAccount.getAccountType().equals(AccountType.SAVINGS)) {
            return createValidationResult(false, SAVINGS_ACCOUNT_WITHDRAWAL_MESSAGE);
        }
        if (MoneyUtil.convertEurosToCents(amount) > fromAccount.getBalance()) {
            return createValidationResult(false, INSUFFICIENT_FUNDS_MESSAGE);
        }
        return createValidationResult(true, "");
    }

    public ValidationResult validateTransfer(User caller, Account fromAccount, Account toAccount, BigDecimal amount) {
        if (!isOwnerOfAccount(caller, fromAccount)) {
            return createValidationResult(false, OWN_ACCOUNT_TRANSFER_MESSAGE);
        }
        if (fromAccount.getAccountType() == AccountType.SAVINGS) {
            return createValidationResult(false, SAVINGS_ACCOUNT_TRANSFER_MESSAGE);
        }

        boolean isInternalTransfer = isOwnerOfAccount(caller, toAccount);
        BigDecimal limit = isInternalTransfer ? internalTransferLimit : externalTransferLimit;
        String message = isInternalTransfer ? INTERNAL_TRANSFER_OVER_LIMIT_MESSAGE : EXTERNAL_TRANSFER_OVER_LIMIT_MESSAGE;

        if (amount.compareTo(limit) > 0) {
            return createValidationResult(false, message);
        }

        return createValidationResult(true, "");
    }


    private boolean isOwnerOfAccount(User user, Account account) {
        return user.getId().equals(account.getOwnerId());
    }

    private ValidationResult createValidationResult(boolean isValid, String message) {
        return new ValidationResult(isValid, message);
    }


}