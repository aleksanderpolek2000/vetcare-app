package com.vetcare.backend.common.exception;

public class CertificateNotFoundException extends RuntimeException {
    public CertificateNotFoundException(String message) {
        super(message);
    }

    private static final String DEFAULT_MESSAGE = "Nie znaleziono certyfikatu";

    public CertificateNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public CertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
