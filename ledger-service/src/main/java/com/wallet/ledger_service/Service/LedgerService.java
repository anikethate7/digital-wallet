package com.wallet.ledger_service.Service;

import com.wallet.ledger_service.DTO.LedgerEntryResponse;

import java.math.BigDecimal;
import java.util.List;

public interface LedgerService {
    void recordDebit(Long transactionId, Long accountId, BigDecimal amount, String currency);
    void recordCredit(Long transactionId, Long accountId, BigDecimal amount, String currency);
    List<LedgerEntryResponse> getEntriesForTransaction(Long transactionId);
}