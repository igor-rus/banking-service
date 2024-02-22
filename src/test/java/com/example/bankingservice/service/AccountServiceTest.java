package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.Role;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.dto.AccountDTO;
import com.example.bankingservice.exception.RecordNotFoundException;
import com.example.bankingservice.mapper.AccountDtoMapper;
import com.example.bankingservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static com.example.bankingservice.domain.AccountType.REGULAR;
import static com.example.bankingservice.domain.AccountType.SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    UserService userService;
    @Mock
    AccountRepository accountRepository;
    @Spy
    AccountDtoMapper accountDtoMapper;
    @InjectMocks
    AccountService accountService;

    @Test
    void accounts_UserCanSeeHisAccountsWithBalances() {
        Principal principal = () -> "user@test.com";

        User user = new User();
        user.setExternalId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userpw");

        Role role = new Role();
        role.setExternalId(1);
        role.setName("ROLE_USER");

        user.setRoles(List.of(role));

        Account regular = new Account();
        regular.setId(1L);
        regular.setAccountNumber("account_number_regular");
        regular.setAccountType(REGULAR);
        regular.setExternalId(1L);
        regular.setBalance(10050L);
        regular.setCurrency("EUR");

        Account savings = new Account();
        savings.setId(2L);
        savings.setAccountNumber("account_number_savings");
        savings.setAccountType(SAVINGS);
        savings.setExternalId(2L);
        savings.setBalance(30000L);
        savings.setCurrency("EUR");

        when(userService.findByUsername(principal.getName())).thenReturn(user);
        when(accountRepository.findByOwnerId(user.getId())).thenReturn(List.of(regular, savings));

        List<AccountDTO> expected = accountService.accounts(principal);

        assertThat(expected).hasSize(2).extracting("balance").contains("100.50", "300.00");
    }


    @Test
    public void findAccountById_WhenNonExistingAccountIsRequestAnErrorIsThrown() {
        Long accountIdInvalid = 0L;
        when(accountRepository.findById(accountIdInvalid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findAccountById(accountIdInvalid))
                .isInstanceOf(RecordNotFoundException.class).hasMessage("Account with ID: 0 not found");
    }

    @Test
    public void updateBalance_WhenUpdatingBalanceCorrectAmountIsUsed() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(100L);

        accountService.updateBalance(200L, account);

        verify(accountRepository, times(1)).updateBalanceForAccount(200L, account.getId());
    }

}