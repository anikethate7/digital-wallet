package com.wallet.ledger_service.Job;

import com.wallet.ledger_service.Repository.LedgerEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class LedgerReconciliationJob {

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerReconciliationJob(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    // Runs every 60 seconds — adjust as needed. In production this might run hourly/nightly.
    @Scheduled(fixedRate = 60000)
    public void reconcile() {
        List<Long> transactionIds = ledgerEntryRepository.findAllDistinctTransactionIds();

        int checked = 0;
        int mismatches = 0;

        for (Long transactionId : transactionIds) {
            BigDecimal totalDebits = ledgerEntryRepository.sumAmountByTransactionIdAndEntryType(transactionId, "DEBIT");
            BigDecimal totalCredits = ledgerEntryRepository.sumAmountByTransactionIdAndEntryType(transactionId, "CREDIT");

            checked++;

            if (totalDebits.compareTo(totalCredits) != 0) {
                mismatches++;
                log.error("LEDGER MISMATCH DETECTED: transactionId={}, totalDebits={}, totalCredits={}",
                        transactionId, totalDebits, totalCredits);
            }
        }

        if (mismatches == 0) {
            log.info("Ledger reconciliation passed: {} transactions checked, 0 mismatches", checked);
        } else {
            log.warn("Ledger reconciliation found {} mismatch(es) out of {} transactions checked", mismatches, checked);
        }
    }
}