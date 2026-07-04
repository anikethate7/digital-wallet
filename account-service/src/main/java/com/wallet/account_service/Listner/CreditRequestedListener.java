package com.wallet.account_service.Listner;

import com.wallet.account_service.Event.CreditCompletedEvent;
import com.wallet.account_service.Event.CreditRequestedEvent;
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

    public CreditRequestedListener(AccountService accountService,
                                   KafkaTemplate<String, CreditCompletedEvent> creditCompletedKafkaTemplate) {
        this.accountService = accountService;
        this.creditCompletedKafkaTemplate = creditCompletedKafkaTemplate;
    }

    @KafkaListener(
            topics = "credit-requested",
            groupId = "wallet-group",
            containerFactory = "creditRequestedKafkaListenerContainerFactory")
    public void handleCreditRequested(CreditRequestedEvent event) {
        log.info("Received CreditRequestedEvent: transactionId={}, from={}, to={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());

        accountService.credit(event.getToAccountId(), event.getAmount());

        CreditCompletedEvent completedEvent = new CreditCompletedEvent(
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());

        creditCompletedKafkaTemplate.send("credit-completed", completedEvent);
        log.info("Published CreditCompletedEvent for transactionId={}", event.getTransactionId());
    }
}