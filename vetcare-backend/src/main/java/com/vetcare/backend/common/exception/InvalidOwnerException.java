package com.vetcare.backend.common.exception;

public class InvalidOwnerException extends RuntimeException {
    public InvalidOwnerException(String message) {
        super(message);
    }

    private static final String DEFAULT_MESSAGE = "Nie znaleziono użytkownika";

    public InvalidOwnerException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
