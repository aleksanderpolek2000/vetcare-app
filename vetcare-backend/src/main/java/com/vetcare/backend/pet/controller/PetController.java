package com.vetcare.backend.pet.controller;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.common.dto.PageResponse;
import com.vetcare.backend.pet.dto.CreatePetRequest;
import com.vetcare.backend.pet.dto.PetResponse;
import com.vetcare.backend.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<@NonNull PetResponse> createPet(@Valid @RequestBody CreatePetRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        PetResponse petResponse = petService.createPet(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(petResponse);
    }

    @GetMapping
    public ResponseEntity<@NonNull PageResponse<PetResponse>> getUserPets(@AuthenticationPrincipal CustomUserDetails userDetails, @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        PageResponse<PetResponse> petResponsePageResponse = petService.getUserPets(userDetails.getUsername(), pageable);

        return ResponseEntity.ok(petResponsePageResponse);
    }

}
