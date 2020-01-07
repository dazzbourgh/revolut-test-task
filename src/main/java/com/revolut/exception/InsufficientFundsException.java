package com.revolut.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(long id) {
        super(String.format("Insufficient funds for account: %s", id));
    }
}
