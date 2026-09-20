package com.vetcare.backend.clinicservicecategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateClinicServiceCategory(@NotBlank @Size(max = 200) String name,
                                          @Size(max = 1000) String description,
                                          @NotNull Integer displayOrder,
                                          @NotNull Boolean active) {
}
