package com.vetcare.backend.pet.controller;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.common.dto.PageResponse;
import com.vetcare.backend.pet.dto.CreatePetRequest;
import com.vetcare.backend.pet.dto.PetResponse;
import com.vetcare.backend.pet.dto.UpdatePetRequest;
import com.vetcare.backend.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody CreatePetRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        PetResponse petResponse = petService.createPet(request, userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(petResponse);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PageResponse<PetResponse>> getUserPets(@AuthenticationPrincipal CustomUserDetails userDetails, @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        PageResponse<PetResponse> petResponsePageResponse = petService.getUserPets(userDetails.getUsername(), pageable);

        return ResponseEntity.ok(petResponsePageResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<PetResponse> updatePet(@PathVariable(name = "id") UUID id, @Valid @RequestBody UpdatePetRequest updatePetRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {

        PetResponse petResponse = petService.updatePet(id, updatePetRequest, userDetails);

        return ResponseEntity.ok(petResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deletePet(@PathVariable(name = "id") UUID id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        petService.deletePet(id, userDetails);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
