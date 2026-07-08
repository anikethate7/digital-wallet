package com.wallet.account_service.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensateDebitEvent {
    private Long transactionId;
    private Long fromAccountId;
    private BigDecimal amount;
}