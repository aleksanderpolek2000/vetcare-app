package com.vetcare.backend.employee.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record AddCertificateRequest(
        @NotNull MultipartFile image,
        @Size(max = 255) String title,
        Integer displayOrder
) {
}