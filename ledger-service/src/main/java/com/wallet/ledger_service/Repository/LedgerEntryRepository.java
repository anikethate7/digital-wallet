package com.wallet.ledger_service.Repository;

import com.wallet.ledger_service.Entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByTransactionId(Long transactionId);
    boolean existsByTransactionIdAndAccountIdAndEntryType(Long transactionId, Long accountId, String entryType);
}
