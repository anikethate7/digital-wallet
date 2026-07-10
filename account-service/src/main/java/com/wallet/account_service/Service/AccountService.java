package com.wallet.account_service.Service;

import com.wallet.account_service.DTO.AccountResponse;
import com.wallet.account_service.DTO.CreateAccountRequest;

import java.math.BigDecimal;


public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request, Long userId);    public AccountResponse getAccount(Long accountId);
    BigDecimal getBalance(Long accountId);
    void debit(Long accountId, BigDecimal amount);
    void credit(Long accountId, BigDecimal amount);
    void freezeAccount(Long accountId);
    void reverseDebit(Long accountId, BigDecimal amount);
}
