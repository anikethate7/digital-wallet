package com.wallet.ledger_service.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LedgerEntryResponse {
    private Long id;
    private Long transactionId;
    private Long accountId;
    private String entryType;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
}
