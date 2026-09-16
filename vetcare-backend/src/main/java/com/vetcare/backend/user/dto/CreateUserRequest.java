package com.vetcare.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@Email @NotBlank String email,
                                @Size(min = 8) @NotBlank String password,
                                String firstName,
                                String lastName) {
}
