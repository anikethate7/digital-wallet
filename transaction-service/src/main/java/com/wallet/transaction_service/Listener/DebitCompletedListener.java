package com.wallet.transaction_service.Listener;

import com.wallet.transaction_service.Event.CreditRequestedEvent;
import com.wallet.transaction_service.Event.DebitCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DebitCompletedListener {

    private final KafkaTemplate<String, CreditRequestedEvent> creditRequestedKafkaTemplate;

    public DebitCompletedListener(KafkaTemplate<String, CreditRequestedEvent> creditRequestedKafkaTemplate) {
        this.creditRequestedKafkaTemplate = creditRequestedKafkaTemplate;
    }

    @KafkaListener(topics = "debit-completed", groupId = "wallet-group")
    public void handleDebitCompleted(DebitCompletedEvent event) {
        log.info("Received DebitCompletedEvent: transactionId={}, from={}, to={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());

        CreditRequestedEvent creditEvent = new CreditRequestedEvent(
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());

        creditRequestedKafkaTemplate.send("credit-requested", creditEvent);
        log.info("Published CreditRequestedEvent for transactionId={}", event.getTransactionId());
    }
}