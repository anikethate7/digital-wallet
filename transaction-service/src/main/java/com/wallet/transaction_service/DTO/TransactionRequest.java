package com.wallet.transaction_service.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
