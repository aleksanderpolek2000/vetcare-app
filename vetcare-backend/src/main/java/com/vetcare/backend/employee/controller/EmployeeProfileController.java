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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @GetMapping
    public ResponseEntity<List<EmployeeProfileResponse>> getAllEmployees(){
        List<EmployeeProfileResponse> employeeProfileResponses =  employeeProfileService.getAllEmployees();

        return ResponseEntity.ok(employeeProfileResponses);
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(employeeProfileService.getMyProfile(userDetails));
    }

    @PreAuthorize("hasAnyRole('VET', 'EMPLOYEE', 'OWNER')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> updateMyProfile(
            @Valid @ModelAttribute UpdateEmployeeProfile request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(employeeProfileService.updateMyProfile(request, userDetails));
    }

    @PreAuthorize("hasAnyRole('VET', 'EMPLOYEE', 'OWNER')")
    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> addCertificate(
            @Valid @ModelAttribute AddCertificateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeProfileService.addCertificate(request, userDetails));
    }
    @PreAuthorize("hasAnyRole('VET', 'EMPLOYEE', 'OWNER')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> createMyProfile(
            @Valid @ModelAttribute UpdateEmployeeProfile request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeProfileService.createMyProfile(request, userDetails));
    }
    @PreAuthorize("hasAnyRole('VET', 'EMPLOYEE', 'OWNER')")
    @DeleteMapping("/certificates/{id}")
    public ResponseEntity<Void> deleteCertificate(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        employeeProfileService.deleteCertificate(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}