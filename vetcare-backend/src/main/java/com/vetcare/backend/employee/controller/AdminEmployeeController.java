package com.vetcare.backend.employee.controller;

import com.vetcare.backend.employee.dto.EmployeeProfileResponse;
import com.vetcare.backend.employee.dto.UpdateEmployeeProfile;
import com.vetcare.backend.employee.service.AdminEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/employees")
@RequiredArgsConstructor
@PreAuthorize("hasRole('OWNER')")
public class AdminEmployeeController {

    private final AdminEmployeeService adminEmployeeManager;

    @PostMapping(value = "/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> createEmployeeForUser(
            @PathVariable UUID userId,
            @Valid @ModelAttribute UpdateEmployeeProfile request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminEmployeeManager.createEmployeeForUser(userId, request));
    }

    @PutMapping(value = "/{employeeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeProfileResponse> updateEmployee(
            @PathVariable UUID employeeId,
            @Valid @ModelAttribute UpdateEmployeeProfile request) {
        return ResponseEntity.ok(adminEmployeeManager.updateEmployee(employeeId, request));
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID employeeId) {
        adminEmployeeManager.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}