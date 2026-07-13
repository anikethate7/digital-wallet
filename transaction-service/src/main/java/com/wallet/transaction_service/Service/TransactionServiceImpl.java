package com.wallet.transaction_service.Service;

import com.wallet.transaction_service.Client.AccountClient;
import com.wallet.transaction_service.DTO.AccountResponse;
import com.wallet.transaction_service.DTO.TransactionRequest;
import com.wallet.transaction_service.DTO.TransactionResponse;
import com.wallet.transaction_service.Entity.Transaction;
import com.wallet.transaction_service.Event.TransactionInitiatedEvent;
import com.wallet.transaction_service.Exception.IdempotencyKeyReusedException;
import com.wallet.transaction_service.Exception.InsufficientFundException;
import com.wallet.transaction_service.Exception.TransactionNotFoundException;
import com.wallet.transaction_service.Exception.UnauthorizedTransferException;
import com.wallet.transaction_service.Repository.TransactionRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;
    private final KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountClient accountClient,
                                  KafkaTemplate<String, TransactionInitiatedEvent> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public TransactionResponse createTransfer(TransactionRequest transactionRequest, String idempotencyKey, Long authenticatedUserId) {
        Optional<Transaction> existing = transactionRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            Transaction existingTransaction = existing.get();

            boolean sameRequest =
                    existingTransaction.getFromAccountId().equals(transactionRequest.getFromAccountId()) &&
                            existingTransaction.getToAccountId().equals(transactionRequest.getToAccountId()) &&
                            existingTransaction.getAmount().compareTo(transactionRequest.getAmount()) == 0;

            if (sameRequest) {
                return toResponse(existingTransaction);
            } else {
                throw new IdempotencyKeyReusedException(
                        "Idempotency key " + idempotencyKey + " was already used with different request data");
            }
        }

        AccountResponse fromAccount = accountClient.getAccountDetails(transactionRequest.getFromAccountId());
        if (!fromAccount.getUserId().equals(authenticatedUserId)) {
            throw new UnauthorizedTransferException(
                    "You do not have permission to transfer from account " + transactionRequest.getFromAccountId());
        }

        BigDecimal balance = accountClient.getBalance(transactionRequest.getFromAccountId());

        if( balance.compareTo(transactionRequest.getAmount()) < 0){
            throw new InsufficientFundException(
                    "Account " + transactionRequest.getFromAccountId() + " has insufficient funds for this transfer"
            );
        }

        Transaction transaction = new Transaction();
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setFromAccountId(transactionRequest.getFromAccountId());
        transaction.setToAccountId(transactionRequest.getToAccountId());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setStatus("INITIATED");
        transaction.setCreatedDate(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);

        TransactionInitiatedEvent event = new TransactionInitiatedEvent(
                saved.getId(), saved.getFromAccountId(), saved.getToAccountId(), saved.getAmount()
        );
        kafkaTemplate.send("transfer-initiated", event);
        return toResponse(saved);
    }

    @Override
    public void markFailed(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transfer not found"));
        transaction.setStatus("FAILED");
        transactionRepository.save(transaction);
    }

    @Override
    public void markCompleted(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setStatus("COMPLETED");
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionResponse getTransfer(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transfer not found"));
        return toResponse(transaction);
    }

    TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setFromAccountId(transaction.getFromAccountId());
        transactionResponse.setToAccountId(transaction.getToAccountId());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setStatus(transaction.getStatus());
        transactionResponse.setCreatedAt(transaction.getCreatedDate());
        return transactionResponse;
    }
}
