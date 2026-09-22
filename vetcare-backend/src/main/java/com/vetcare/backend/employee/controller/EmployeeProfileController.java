package com.vetcare.backend.employee.controller;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.employee.dto.AddCertificateRequest;
import com.vetcare.backend.employee.dto.EmployeeProfileResponse;
import com.vetcare.backend.employee.dto.UpdateEmployeeProfile;
import com.vetcare.backend.employee.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees/me")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('VET', 'EMPLOYEE', 'OWNER')")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileManager;

    @GetMapping
    public ResponseEntity<EmployeeProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(employeeProfileManager.getMyProfile(userDetails));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> updateMyProfile(
            @Valid @ModelAttribute UpdateEmployeeProfile request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(employeeProfileManager.updateMyProfile(request, userDetails));
    }

    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> addCertificate(
            @Valid @ModelAttribute AddCertificateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeProfileManager.addCertificate(request, userDetails));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> createMyProfile(
            @Valid @ModelAttribute UpdateEmployeeProfile request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeProfileManager.createMyProfile(request, userDetails));
    }

    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<Void> deleteCertificate(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        employeeProfileManager.deleteCertificate(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}