package com.wallet.ledger_service.Listener;


import com.wallet.ledger_service.Event.DebitCompletedEvent;
import com.wallet.ledger_service.Service.LedgerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class DebitCompletedListener {

    private final LedgerService ledgerService;

    public DebitCompletedListener(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @KafkaListener(
            topics = "debit-completed",
            groupId = "wallet-group",
            containerFactory = "debitCompletedKafkaListenerContainerFactory"
    )
    public void handleDebitCompleted(DebitCompletedEvent event) {
        log.info("Received DebitCompletedEvent: transactionId={}, account={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getAmount());

        ledgerService.recordDebit(event.getTransactionId(), event.getFromAccountId(), event.getAmount(), "INR");

    }
}
