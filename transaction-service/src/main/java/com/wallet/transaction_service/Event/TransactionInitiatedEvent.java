package com.wallet.transaction_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInitiatedEvent {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
