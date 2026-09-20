package com.vetcare.backend.common.exception;

public class InvalidServiceCategoryException extends RuntimeException {
    public InvalidServiceCategoryException(String message) {
        super(message);
    }

    private static final String DEFAULT_MESSAGE = "Nie znaleziono kategorii";

    public InvalidServiceCategoryException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidServiceCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
