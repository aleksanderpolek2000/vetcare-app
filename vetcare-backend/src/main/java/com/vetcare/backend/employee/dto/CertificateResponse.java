package com.vetcare.backend.employee.dto;

import java.util.UUID;

public record CertificateResponse(
        UUID id,
        String imagePath,
        String title,
        Integer displayOrder
) {
}