package com.example.bankingservice.repository;

import com.example.bankingservice.domain.LedgerRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRecordRepository extends CrudRepository<LedgerRecord, Long> {
}
