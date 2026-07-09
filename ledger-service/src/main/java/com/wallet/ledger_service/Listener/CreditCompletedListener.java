package com.wallet.ledger_service.Listener;

import com.wallet.ledger_service.Event.CreditCompletedEvent;
import com.wallet.ledger_service.Service.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreditCompletedListener {

    private final LedgerService ledgerService;

    public CreditCompletedListener(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @KafkaListener(
            topics = "credit-completed",
            groupId = "wallet-group",
            containerFactory = "creditCompletedKafkaListenerContainerFactory")
    public void handleCreditCompleted(CreditCompletedEvent event) {
        log.info("Received CreditCompletedEvent: transactionId={}, account={}, amount={}",
                event.getTransactionId(), event.getToAccountId(), event.getAmount());

        ledgerService.recordCredit(event.getTransactionId(), event.getToAccountId(), event.getAmount(), "INR");
    }
}