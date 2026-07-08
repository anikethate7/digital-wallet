package com.wallet.account_service.Listener;

import com.wallet.account_service.Event.CreditCompletedEvent;
import com.wallet.account_service.Event.CreditFailedEvent;
import com.wallet.account_service.Event.CreditRequestedEvent;
import com.wallet.account_service.Exception.AccountFrozenException;
import com.wallet.account_service.Service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditRequestedListener {

    private final AccountService accountService;
    private final KafkaTemplate<String, CreditCompletedEvent> creditCompletedKafkaTemplate;
    private final KafkaTemplate<String, CreditFailedEvent> creditFailedKafkaTemplate;

    public CreditRequestedListener(AccountService accountService,
                                   KafkaTemplate<String, CreditCompletedEvent> creditCompletedKafkaTemplate,
                                   KafkaTemplate<String, CreditFailedEvent> creditFailedKafkaTemplate) {
        this.accountService = accountService;
        this.creditCompletedKafkaTemplate = creditCompletedKafkaTemplate;
        this.creditFailedKafkaTemplate = creditFailedKafkaTemplate;
    }

    @KafkaListener(
            topics = "credit-requested",
            groupId = "wallet-group",
            containerFactory = "creditRequestedKafkaListenerContainerFactory")
    public void handleCreditRequested(CreditRequestedEvent event) {
        log.info("Received CreditRequestedEvent: transactionId={}, from={}, to={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());

        try {
            accountService.credit(event.getToAccountId(), event.getAmount());

            CreditCompletedEvent completedEvent = new CreditCompletedEvent(
                    event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());
            creditCompletedKafkaTemplate.send("credit-completed", completedEvent);
            log.info("Published CreditCompletedEvent for transactionId={}", event.getTransactionId());

        } catch (AccountFrozenException ex) {
            log.warn("Credit failed for transactionId={}: {}", event.getTransactionId(), ex.getMessage());

            CreditFailedEvent failedEvent = new CreditFailedEvent(
                    event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(),
                    event.getAmount(), ex.getMessage());
            creditFailedKafkaTemplate.send("credit-failed", failedEvent);
            log.info("Published CreditFailedEvent for transactionId={}", event.getTransactionId());
        }
    }
}