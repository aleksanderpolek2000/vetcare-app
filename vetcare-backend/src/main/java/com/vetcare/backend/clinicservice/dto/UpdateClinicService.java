package com.vetcare.backend.clinicservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdateClinicService(@NotBlank @Size(max = 200) String name,
                                  @Size(max = 2000) String description,
                                  @NotNull Integer displayOrder,
                                  @NotNull Boolean active,
                                  MultipartFile image,
                                  @NotNull UUID categoryId
) {
}
