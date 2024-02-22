package com.example.bankingservice.mapper;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.bankingservice.domain.AccountType.REGULAR;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountDtoMapperTest {

    AccountDtoMapper mapper = new AccountDtoMapper();

    @Test
    void mapperWorks() {
        Account regular = new Account();
        regular.setId(1L);
        regular.setAccountNumber("account_number_regular");
        regular.setAccountType(REGULAR);
        regular.setExternalId(1L);
        regular.setBalance(10050L);
        regular.setCurrency("EUR");

        AccountDTO expected = mapper.toDTO(regular);
        assertThat(expected.externalId()).isEqualTo(1L);
        assertThat(expected.accountNumber()).isEqualTo("account_number_regular");
        assertThat(expected.type()).isEqualTo("REGULAR");
        assertThat(expected.balance()).isEqualTo("100.50");
        assertThat(expected.currency()).isEqualTo("EUR");
    }
}