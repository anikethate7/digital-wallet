package com.wallet.account_service.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private Long userId;
    private String accountType;
    private BigDecimal initialBalance;
    private String currency;

}
