package com.wallet.transaction_service.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AccountResponse {
    private Long id;
    private Long userId;
    private String accountNumber;
    private BigDecimal balance;
    private String status;
}