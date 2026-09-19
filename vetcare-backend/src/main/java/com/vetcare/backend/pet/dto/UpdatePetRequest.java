package com.vetcare.backend.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdatePetRequest(@NotBlank(message = "Imię nie może być puste")
                               @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
                               String name,

                               @NotBlank(message = "Gatunek jest wymagany")
                               @Size(max = 50, message = "Nazwa gatunku nie może przekraczać 50 znaków")
                               String species,

                               @Size(max = 50, message = "Rasa nie może przekraczać 50 znaków")
                               String breed,

                               @NotNull(message = "Data urodzenia jest wymagana")
                               @PastOrPresent(message = "Data urodzenia nie może być z przyszłości")
                               LocalDateTime dateOfBirth) {
}
