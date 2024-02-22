package com.example.bankingservice.repository;

import com.example.bankingservice.domain.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByOwnerId(Long ownerId);
    Optional<Account> findByIdAndOwnerId(Long id, Long ownerId);
    @Modifying
    @Query("update Account a set a.balance = :balance where a.id = :id")
    void updateBalanceForAccount(@Param(value = "balance") Long balance, @Param(value="id") Long accountId);
}
