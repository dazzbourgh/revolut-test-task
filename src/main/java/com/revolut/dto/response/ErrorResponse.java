package com.revolut.dto.response;

public class ErrorResponse extends Response {
    private boolean error;

    public ErrorResponse(String message) {
        super(message);
        error = true;
    }
}
