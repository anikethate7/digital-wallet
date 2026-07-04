package com.wallet.account_service.Service;

import com.wallet.account_service.DTO.AccountResponse;
import com.wallet.account_service.DTO.CreateAccountRequest;
import com.wallet.account_service.Entity.Account;
import com.wallet.account_service.Exception.AccountNotFoundException;
import com.wallet.account_service.Exception.InsufficientFundException;
import com.wallet.account_service.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setUserId(request.getUserId());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialBalance());
        account.setCurrency(request.getCurrency());
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);

        saved.setAccountNumber(String.format("ACC%010d", saved.getId()));
        Account finalAccount = accountRepository.save(saved);

        return toResponse(finalAccount);
    }

    @Override
    public AccountResponse getAccount(Long accountId) {
        return toResponse(findAccountOrThrow(accountId));
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        return findAccountOrThrow(accountId).getBalance();
    }

    @Override
    public void debit(Long accountId, BigDecimal amount) {
        Account account = findAccountOrThrow(accountId);
        if(account.getBalance().compareTo(amount) <= 0){
            throw new InsufficientFundException("Account " + accountId + " has insufficient funds");
        }

        account.setBalance(account.getBalance().subtract(amount));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void credit(Long accountId, BigDecimal amount) {
        Account account = findAccountOrThrow(accountId);
        account.setBalance(account.getBalance().add(amount));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    private Account findAccountOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id: " + id + " not found"));
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setUserId(account.getUserId());
        accountResponse.setAccountType(account.getAccountType());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setCurrency(account.getCurrency());
        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.setAccountNumber(account.getAccountNumber());
        accountResponse.setStatus(account.getStatus());

        return accountResponse;
    }


}
