package com.wallet.ledger_service.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ledger_entries",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_transaction_account_type",
                columnNames = {"transaction_id", "account_id", "entry_type"}
        )
)
@Data
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId;

    private Long accountId;

    private String entryType;

    private BigDecimal amount;

    private String currency;

    private LocalDateTime createdAt;
}