package com.vetcare.backend.clinicservicecategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClinicServiceCategory(@NotBlank @Size(max = 200) String name,
                                          @Size(max = 1000) String description) {
}
