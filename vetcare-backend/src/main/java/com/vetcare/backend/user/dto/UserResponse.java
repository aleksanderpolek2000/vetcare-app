package com.vetcare.backend.user.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id,
                           String email,
                           String firstName,
                           String lastName,
                           Set<String> roles,
                           LocalDateTime createdAt) {
}
