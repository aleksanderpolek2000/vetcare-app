package com.vetcare.backend.clinicservicecategory.controller;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.clinicservicecategory.dto.ClinicServiceCategoryResponse;
import com.vetcare.backend.clinicservicecategory.dto.CreateClinicServiceCategory;
import com.vetcare.backend.clinicservicecategory.dto.UpdateClinicServiceCategory;
import com.vetcare.backend.clinicservicecategory.service.ClinicServiceCategoryManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinic-service-categories")
@RequiredArgsConstructor
public class ClinicServiceCategoryController {

    private final ClinicServiceCategoryManager clinicServiceCategoryManager;

    @GetMapping
    public ResponseEntity<List<ClinicServiceCategoryResponse>> getClinicServiceCategory() {
        return ResponseEntity.ok(clinicServiceCategoryManager.getClinicServiceCategories());
    }

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ClinicServiceCategoryResponse> createClinicServiceCategory(@Valid @RequestBody CreateClinicServiceCategory categoryRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {

        ClinicServiceCategoryResponse categoryResponse = clinicServiceCategoryManager.createClinicServiceCategory(categoryRequest, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ClinicServiceCategoryResponse> updateClinicServiceCategory(@PathVariable(name = "id") UUID id, @Valid @RequestBody UpdateClinicServiceCategory categoryRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {

        ClinicServiceCategoryResponse categoryResponse = clinicServiceCategoryManager.updateClinicServiceCategory(id, categoryRequest, userDetails);

        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteClinicServiceCategory (@PathVariable(name = "id") UUID id){
        clinicServiceCategoryManager.deleteClinicServiceCategory(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
