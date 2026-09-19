package com.vetcare.backend.clinicservice.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClinicServiceResponse(UUID id,
                                    String name,
                                    String description,
                                    String image,
                                    String slug,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt,
                                    UUID categoryId) {
}
