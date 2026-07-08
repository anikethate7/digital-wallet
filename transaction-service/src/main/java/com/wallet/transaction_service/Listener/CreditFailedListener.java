package com.wallet.transaction_service.Listener;

import com.wallet.transaction_service.Event.CompensateDebitEvent;
import com.wallet.transaction_service.Event.CreditFailedEvent;
import com.wallet.transaction_service.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditFailedListener {

    private final TransactionService transactionService;
    private final KafkaTemplate<String, CompensateDebitEvent> compensateDebitKafkaTemplate;

    public CreditFailedListener(TransactionService transactionService,
                                KafkaTemplate<String, CompensateDebitEvent> compensateDebitKafkaTemplate) {
        this.transactionService = transactionService;
        this.compensateDebitKafkaTemplate = compensateDebitKafkaTemplate;
    }

    @KafkaListener(
            topics = "credit-failed",
            groupId = "wallet-group",
            containerFactory = "creditFailedKafkaListenerContainerFactory")
    public void handleCreditFailed(CreditFailedEvent event) {
        log.warn("Received CreditFailedEvent: transactionId={}, reason={}",
                event.getTransactionId(), event.getReason());

        transactionService.markFailed(event.getTransactionId());

        CompensateDebitEvent compensateEvent = new CompensateDebitEvent(
                event.getTransactionId(), event.getFromAccountId(), event.getAmount());
        compensateDebitKafkaTemplate.send("compensate-debit", compensateEvent);
        log.info("Published CompensateDebitEvent for transactionId={}", event.getTransactionId());
    }
}