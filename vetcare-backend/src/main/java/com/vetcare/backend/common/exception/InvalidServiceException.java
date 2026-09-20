package com.vetcare.backend.common.exception;

public class InvalidServiceException extends RuntimeException {
    public InvalidServiceException(String message) {
        super(message);
    }

    private static final String DEFAULT_MESSAGE = "Nie znaleziono usługi";

    public InvalidServiceException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
