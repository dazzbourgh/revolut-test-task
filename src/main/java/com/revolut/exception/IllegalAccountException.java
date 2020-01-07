package com.revolut.exception;

public class IllegalAccountException extends RuntimeException {
    public IllegalAccountException(long id) {
        super(String.format("Account does not exist: %s", id));
    }
}
