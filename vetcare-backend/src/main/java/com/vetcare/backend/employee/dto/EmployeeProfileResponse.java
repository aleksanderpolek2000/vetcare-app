package com.vetcare.backend.employee.dto;

import java.util.List;
import java.util.UUID;

public record EmployeeProfileResponse(
        UUID id,
        String firstName,
        String lastName,
        String description,
        String profileImage,
        List<CertificateResponse> certificates
) {
}