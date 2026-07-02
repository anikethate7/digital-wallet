package com.wallet.transaction_service.Service;

import com.wallet.transaction_service.DTO.TransactionRequest;
import com.wallet.transaction_service.DTO.TransactionResponse;
import com.wallet.transaction_service.Entity.Transaction;
import com.wallet.transaction_service.Exception.TransactionNotFoundException;
import com.wallet.transaction_service.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResponse createTransfer(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(transactionRequest.getFromAccountId());
        transaction.setToAccountId(transactionRequest.getToAccountId());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setStatus("INITIATED");
        transaction.setCreatedDate(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);

        return toResponse(saved);

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
