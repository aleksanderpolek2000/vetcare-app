package com.vetcare.backend.common.exception;

public class InvalidOwnerException extends RuntimeException {
    public InvalidOwnerException(String message) {
        super(message);
    }

    public InvalidOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
