package com.wallet.ledger_service.Service;

import com.wallet.ledger_service.DTO.LedgerEntryResponse;
import com.wallet.ledger_service.Entity.LedgerEntry;
import com.wallet.ledger_service.Repository.LedgerEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LedgerServiceImpl implements LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerServiceImpl(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @Override
    public void recordDebit(Long transactionId, Long accountId, BigDecimal amount, String currency) {
        recordEntry(transactionId, accountId, "DEBIT", amount, currency);
    }

    @Override
    public void recordCredit(Long transactionId, Long accountId, BigDecimal amount, String currency) {
        recordEntry(transactionId, accountId, "CREDIT", amount, currency);
    }

    @Override
    public List<LedgerEntryResponse> getEntriesForTransaction(Long transactionId) {
        return ledgerEntryRepository.findByTransactionId(transactionId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void recordEntry(Long transactionId, Long accountId, String entryType,
                        BigDecimal amount, String currency){
        boolean alreadyExists = ledgerEntryRepository.existsByTransactionIdAndAccountIdAndEntryType(transactionId, accountId, entryType);

        if(alreadyExists){
            log.warn("Duplicate {} event ignored for transactionId={}, accountId={}", entryType, transactionId, accountId);
            return;
        }

        LedgerEntry entry  = new LedgerEntry();
        entry.setTransactionId(transactionId);
        entry.setAccountId(accountId);
        entry.setEntryType(entryType);
        entry.setAmount(amount);
        entry.setCurrency(currency);
        entry.setCreatedAt(LocalDateTime.now());

        ledgerEntryRepository.save(entry);
        log.info("Recorded {} entry: transactionId={}, accountId={}, amount={}", entryType, transactionId, accountId, amount);
    }

    private LedgerEntryResponse toResponse(LedgerEntry entry){
        LedgerEntryResponse response = new LedgerEntryResponse();
        response.setId(entry.getId());
        response.setTransactionId(entry.getTransactionId());
        response.setAccountId(entry.getAccountId());
        response.setEntryType(entry.getEntryType());
        response.setAmount(entry.getAmount());
        response.setCurrency(entry.getCurrency());
        response.setCreatedAt(entry.getCreatedAt());
        return response;
    }
}
