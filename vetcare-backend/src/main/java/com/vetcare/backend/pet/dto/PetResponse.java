package com.vetcare.backend.pet.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PetResponse(UUID id,
                          String name,
                          String species,
                          String breed,
                          LocalDateTime dateOfBirth,
                          UUID ownerId,
                          LocalDateTime createdAt,
                          LocalDateTime modifiedAt) {
}
