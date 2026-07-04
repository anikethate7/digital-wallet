package com.wallet.transaction_service.Listener;

import com.wallet.transaction_service.Event.CreditCompletedEvent;
import com.wallet.transaction_service.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditCompletedListener {

    private final TransactionService transactionService;

    public CreditCompletedListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(
            topics = "credit-completed",
            groupId = "wallet-group",
            containerFactory = "creditCompletedKafkaListenerContainerFactory")
    public void handleCreditCompleted(CreditCompletedEvent event) {
        log.info("Received CreditCompletedEvent: transactionId={}", event.getTransactionId());
        transactionService.markCompleted(event.getTransactionId());
        log.info("Transaction {} marked as COMPLETED", event.getTransactionId());
    }
}