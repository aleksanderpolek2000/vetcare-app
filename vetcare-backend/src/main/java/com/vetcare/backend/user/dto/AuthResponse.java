package com.vetcare.backend.user.dto;

public record AuthResponse(String token,
                           String email) {
}
