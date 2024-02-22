package com.example.bankingservice.mapper;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.dto.AccountDTO;
import org.springframework.stereotype.Component;

import static com.example.bankingservice.util.MoneyUtil.formatCentsAsEuros;

@Component
public class AccountDtoMapper implements Mapper<Account, AccountDTO> {

    public AccountDTO toDTO(Account account) {
        return new AccountDTO(
                account.getExternalId(),
                account.getAccountNumber(),
                account.getAccountType().toString(),
                formatCentsAsEuros(account.getBalance()),
                account.getCurrency()
        );
    }


}

