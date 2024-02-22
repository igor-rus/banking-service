package com.example.bankingservice.exception;

import java.io.Serial;

public class OperationNotAllowedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OperationNotAllowedException(String customMessage) {
        super(customMessage);
    }
}
