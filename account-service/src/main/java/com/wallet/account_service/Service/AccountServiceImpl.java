package com.wallet.account_service.Service;

import com.wallet.account_service.DTO.AccountResponse;
import com.wallet.account_service.DTO.CreateAccountRequest;
import com.wallet.account_service.Entity.Account;
import com.wallet.account_service.Exception.AccountFrozenException;
import com.wallet.account_service.Exception.AccountNotFoundException;
import com.wallet.account_service.Exception.InsufficientFundException;
import com.wallet.account_service.Repository.AccountRepository;
import jakarta.persistence.EntityManager;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@Service
public class AccountServiceImpl implements AccountService{

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 50;

    private final AccountRepository accountRepository;
    private final EntityManager entityManager;

    public AccountServiceImpl(AccountRepository accountRepository, EntityManager entityManager) {
        this.accountRepository = accountRepository;
        this.entityManager = entityManager;
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
//        Account account = findAccountOrThrow(accountId);
//        if(account.getBalance().compareTo(amount) <= 0){
//            throw new InsufficientFundException("Account " + accountId + " has insufficient funds");
//        }
//
//        account.setBalance(account.getBalance().subtract(amount));
//        account.setUpdatedAt(LocalDateTime.now());
//        accountRepository.save(account);
        executeWithRetry(accountId, account -> {
            if(account.getBalance().compareTo(amount) <= 0){
                throw new InsufficientFundException("Account " + accountId +" has Insufficient Fund");
            }
            account.setBalance(account.getBalance().subtract(amount));
        });
    }

    @Override
    public void credit(Long accountId, BigDecimal amount) {
//        Account account = findAccountOrThrow(accountId);
//
//        if ("FROZEN".equals(account.getStatus())) {
//            throw new AccountFrozenException("Account " + accountId + " is frozen, cannot credit");
//        }
//
//        account.setBalance(account.getBalance().add(amount));
//        account.setUpdatedAt(LocalDateTime.now());
//        accountRepository.save(account);
        executeWithRetry(accountId, account -> {
            if("FROZEN".equals(account.getStatus())){
                throw new AccountFrozenException("Account " + accountId + " has been frozen, cannot credit");
            }
            account.setBalance(account.getBalance().add(amount));
        });

    }

    @Override
    public void freezeAccount(Long accountId) {
        Account account = findAccountOrThrow(accountId);
        account.setStatus("FROZEN");
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void reverseDebit(Long accountId, BigDecimal amount) {
       executeWithRetry(accountId, account->{
           account.setBalance(account.getBalance().add(amount));
       });
    }

    private void executeWithRetry(Long accountId, Consumer<Account> mutation) {
        int attempts = 0;
        while (true) {
            try {
                Account account = findAccountOrThrow(accountId);
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " read balance: " + account.getBalance() + ", version: " + account.getVersion());

                mutation.accept(account);
                account.setUpdatedAt(LocalDateTime.now());
                accountRepository.save(account);

                System.out.println("Thread " + Thread.currentThread().getName() + " SUCCEEDED");
                return;

            } catch (ObjectOptimisticLockingFailureException ex) {
                attempts++;
                System.out.println("Thread " + Thread.currentThread().getName() +
                        " got optimistic lock conflict, attempt " + attempts);
                entityManager.clear();
                if (attempts >= MAX_RETRIES) {
                    throw new IllegalStateException("Failed after retries");
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
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
