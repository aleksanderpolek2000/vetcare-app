package com.vetcare.backend.employee.service;

import com.vetcare.backend.common.exception.EmployeeNotFoundException;
import com.vetcare.backend.common.exception.InvalidOwnerException;
import com.vetcare.backend.common.storage.FileStorageService;
import com.vetcare.backend.employee.dto.EmployeeProfileResponse;
import com.vetcare.backend.employee.dto.UpdateEmployeeProfile;
import com.vetcare.backend.employee.model.EmployeeEntity;
import com.vetcare.backend.employee.repository.EmployeeRepository;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final EmployeeProfileService helperManager;

    @Transactional
    public EmployeeProfileResponse createEmployeeForUser(UUID userId, UpdateEmployeeProfile request) {
        if (employeeRepository.existsByUser_Id(userId)) {
            throw new IllegalStateException("Ten użytkownik ma już przypisany profil pracownika.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(InvalidOwnerException::new);

        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .description(request.description())
                .user(user)
                .build();

        if (request.profileImage() != null && !request.profileImage().isEmpty()) {
            String imagePath = fileStorageService.storeFile(request.profileImage(), "employees/profiles");
            newEmployee.setProfileImage(imagePath);
        }

        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        return helperManager.mapToResponse(savedEmployee);
    }

    @Transactional
    public EmployeeProfileResponse updateEmployee(UUID employeeId, UpdateEmployeeProfile request) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        employee.setFirstName(request.firstName());
        employee.setLastName(request.lastName());
        employee.setDescription(request.description());

        if (request.profileImage() != null && !request.profileImage().isEmpty()) {
            if (employee.getProfileImage() != null) {
                fileStorageService.deleteFile(employee.getProfileImage());
            }
            String newImagePath = fileStorageService.storeFile(request.profileImage(), "employees/profiles");
            employee.setProfileImage(newImagePath);
        }

        return helperManager.mapToResponse(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(EmployeeNotFoundException::new);

        if (employee.getProfileImage() != null) {
            fileStorageService.deleteFile(employee.getProfileImage());
        }
        employee.getCertificates().forEach(cert -> fileStorageService.deleteFile(cert.getImagePath()));

        employeeRepository.delete(employee);
    }
}