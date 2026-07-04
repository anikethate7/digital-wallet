package com.wallet.account_service.Listner;

import com.wallet.account_service.Event.TransactionInitiatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TransactionEventListener {

    @KafkaListener(topics = "transfer-initiated", groupId = "wallet-group")
    public void handleTransactionInitiated(TransactionInitiatedEvent event) {
        log.info("Received TransferInitiatedEvent: transactionId={}, from={}, to={}, amount={}",
                event.getTransactionId(), event.getFromAccountId(), event.getToAccountId(), event.getAmount());
    }
}
