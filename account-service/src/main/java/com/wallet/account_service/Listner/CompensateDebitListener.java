package com.wallet.account_service.Listner;

import com.wallet.account_service.Event.CompensateDebitEvent;
import com.wallet.account_service.Event.DebitReversedEvent;
import com.wallet.account_service.Service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CompensateDebitListener {

    private final AccountService accountService;
    private final KafkaTemplate<String, DebitReversedEvent> debitReversedKafkaTemplate;

    public CompensateDebitListener(AccountService accountService,
                                   KafkaTemplate<String, DebitReversedEvent> debitReversedKafkaTemplate) {
        this.accountService = accountService;
        this.debitReversedKafkaTemplate = debitReversedKafkaTemplate;
    }

    @KafkaListener(
            topics = "compensate-debit",
            groupId = "wallet-group",
            containerFactory = "compensateDebitKafkaListenerContainerFactory")
    public void handleCompensateDebit(CompensateDebitEvent event) {
        log.info("Received CompensateDebitEvent: transactionId={}, account={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getAmount());

        accountService.reverseDebit(event.getFromAccountId(), event.getAmount());

        DebitReversedEvent reversedEvent = new DebitReversedEvent(
                event.getTransactionId(), event.getFromAccountId(), event.getAmount());
        debitReversedKafkaTemplate.send("debit-reversed", reversedEvent);
        log.info("Published DebitReversedEvent for transactionId={}", event.getTransactionId());
    }
}