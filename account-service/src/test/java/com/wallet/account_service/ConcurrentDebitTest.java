package com.wallet.account_service;

import com.wallet.account_service.Entity.Account;
import com.wallet.account_service.Repository.AccountRepository;
import com.wallet.account_service.Service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcurrentDebitTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Long testAccountId;

    @BeforeEach
    void setup() {
        Account account = new Account();
        account.setUserId(999L);
        account.setAccountType("SAVINGS");
        account.setBalance(new BigDecimal("1000.00")); // starting balance
        account.setCurrency("INR");
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        Account saved = accountRepository.save(account);
        testAccountId = saved.getId();
    }

    @Test
    void twoSimultaneousDebitsShouldNotOverdraw() throws InterruptedException {
        BigDecimal debitAmount = new BigDecimal("700.00"); // two of these = 1400, exceeds 1000 balance

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1); // holds both threads until released together
        CountDownLatch doneLatch = new CountDownLatch(2);

        Runnable debitTask = () -> {
            try {
                startLatch.await(); // wait for the signal so both threads fire at nearly the same instant
                accountService.debit(testAccountId, debitAmount);
            } catch (Exception e) {
                System.out.println("Debit failed as expected for one thread: " + e.getMessage());
            } finally {
                doneLatch.countDown();
            }
        };

        executor.submit(debitTask);
        executor.submit(debitTask);

        startLatch.countDown(); // release both threads simultaneously
        doneLatch.await(); // wait for both to finish

        Account finalAccount = accountRepository.findById(testAccountId).orElseThrow();

        System.out.println("Final balance: " + finalAccount.getBalance());

        // Balance should be either 1000 (both failed) or 300 (only one succeeded) — NEVER negative
        assertEquals(true,
                finalAccount.getBalance().compareTo(BigDecimal.ZERO) >= 0,
                "Balance should never go negative — over-withdrawal occurred!");

        executor.shutdown();
    }
}