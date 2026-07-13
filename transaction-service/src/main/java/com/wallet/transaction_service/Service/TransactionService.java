package com.wallet.transaction_service.Service;

import com.wallet.transaction_service.DTO.TransactionRequest;
import com.wallet.transaction_service.DTO.TransactionResponse;
import org.springframework.stereotype.Service;


public interface TransactionService {
    TransactionResponse createTransfer(TransactionRequest request, String idempotencyKey, Long authenticatedUserId);    TransactionResponse getTransfer(Long id);
    void markCompleted(Long transactionId);
    public void markFailed(Long transactionId);
}
