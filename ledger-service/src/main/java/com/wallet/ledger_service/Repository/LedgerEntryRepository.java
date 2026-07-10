package com.wallet.ledger_service.Repository;

import com.wallet.ledger_service.Entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByTransactionId(Long transactionId);
    boolean existsByTransactionIdAndAccountIdAndEntryType(Long transactionId, Long accountId, String entryType);

    @Query("SELECT DISTINCT l.transactionId FROM LedgerEntry l")
    List<Long> findAllDistinctTransactionIds();

    @Query("SELECT COALESCE(SUM(l.amount), 0) FROM LedgerEntry l WHERE l.transactionId = :transactionId AND l.entryType = :entryType")
    BigDecimal sumAmountByTransactionIdAndEntryType(Long transactionId, String entryType);
}
