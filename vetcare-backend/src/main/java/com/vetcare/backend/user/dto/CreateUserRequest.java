package com.vetcare.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@Email @NotBlank String email,
                                @Size(min = 8, max = 128) @NotBlank String password,
                                @NotBlank @Size(max = 32) String firstName,
                                @NotBlank @Size(max = 32) String lastName) {
}
