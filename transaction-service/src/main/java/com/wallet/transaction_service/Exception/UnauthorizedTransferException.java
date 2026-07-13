package com.wallet.transaction_service.Exception;

public class UnauthorizedTransferException extends RuntimeException {
    public UnauthorizedTransferException(String message) {
        super(message);
    }
}