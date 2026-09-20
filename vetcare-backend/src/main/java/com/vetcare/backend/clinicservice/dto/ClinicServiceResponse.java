package com.vetcare.backend.clinicservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClinicServiceResponse(UUID id,
                                    String name,
                                    String description,
                                    String image,
                                    String slug,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt,
                                    Integer displayOrder,
                                    UUID createdBy,
                                    Boolean active,
                                    UUID categoryId) {
}
