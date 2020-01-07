package com.revolut.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends Response {
    private boolean error;

    public ErrorResponse(String message) {
        super(message);
        error = true;
    }
}
