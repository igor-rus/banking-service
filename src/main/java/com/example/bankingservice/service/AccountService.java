package com.example.bankingservice.service;

import com.example.bankingservice.domain.Account;
import com.example.bankingservice.domain.User;
import com.example.bankingservice.dto.AccountDTO;
import com.example.bankingservice.exception.RecordNotFoundException;
import com.example.bankingservice.mapper.Mapper;
import com.example.bankingservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final Mapper<Account, AccountDTO> accountDtoMapper;

    public List<AccountDTO> accounts(Principal principal) {
        User owner = userService.findByUsername(principal.getName());
        List<Account> accounts = accountRepository.findByOwnerId(owner.getId());

        return accounts.stream().map(accountDtoMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<AccountDTO> accountDetails(Principal principal, Long accountId) {
        User owner = userService.findByUsername(principal.getName());
        Optional<Account> account = accountRepository.findByIdAndOwnerId(accountId, owner.getId());

        return account.map(accountDtoMapper::toDTO);
    }

    public Account findAccountById(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account with ID: %s not found", accountId)
                ));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void updateBalance(Long balance, Account account) {
        accountRepository.updateBalanceForAccount(balance, account.getId());
    }
}
