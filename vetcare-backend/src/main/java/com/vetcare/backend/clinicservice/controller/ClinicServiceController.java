package com.vetcare.backend.clinicservice.controller;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.clinicservice.dto.ClinicServiceResponse;
import com.vetcare.backend.clinicservice.dto.CreateClinicService;
import com.vetcare.backend.clinicservice.dto.UpdateClinicService;
import com.vetcare.backend.clinicservice.service.ClinicServiceManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clinic-services")
@RequiredArgsConstructor
public class ClinicServiceController {

    private final ClinicServiceManager clinicServiceManager;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ClinicServiceResponse>> getClinicServicesList(@PathVariable(name = "categoryId") UUID categoryId) {

        List<ClinicServiceResponse> serviceResponseList = clinicServiceManager.getClinicServicesList(categoryId);

        return ResponseEntity.ok(serviceResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicServiceResponse> getClinicService(@PathVariable(name = "id") UUID id) {

        ClinicServiceResponse serviceResponse = clinicServiceManager.getClinicService(id);

        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ClinicServiceResponse> createClinicService(@Valid @ModelAttribute CreateClinicService clinicServiceRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ClinicServiceResponse serviceResponse = clinicServiceManager.createClinicService(clinicServiceRequest, userDetails);

        return ResponseEntity.ok(serviceResponse);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ClinicServiceResponse> updateClinicService(@PathVariable(name = "id") UUID id, @Valid @ModelAttribute UpdateClinicService clinicServiceRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {

        ClinicServiceResponse serviceResponse = clinicServiceManager.updateClinicService(id, clinicServiceRequest, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(serviceResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteClinicService(@PathVariable(name = "id") UUID id) {
        clinicServiceManager.deleteClinicService(id);

        return ResponseEntity.noContent().build();
    }

}
