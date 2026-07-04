package com.wallet.transaction_service.Exception;

public class InsufficientFundException extends RuntimeException{
    public InsufficientFundException(String message){
        super(message);
    }
}
