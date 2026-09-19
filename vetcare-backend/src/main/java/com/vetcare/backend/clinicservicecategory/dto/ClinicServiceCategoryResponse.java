package com.vetcare.backend.clinicservicecategory.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClinicServiceCategoryResponse(UUID id,
                                            String name,
                                            String description,
                                            String slug,
                                            LocalDateTime createdAt,
                                            LocalDateTime updatedAt,
                                            Integer displayOrder,
                                            Boolean active) {
}
