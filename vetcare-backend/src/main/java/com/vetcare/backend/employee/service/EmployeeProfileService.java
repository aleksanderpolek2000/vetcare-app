package com.vetcare.backend.employee.service;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.common.exception.CertificateNotFoundException;
import com.vetcare.backend.common.exception.EmployeeNotFoundException;
import com.vetcare.backend.common.exception.InvalidOwnerException;
import com.vetcare.backend.common.storage.FileStorageService;
import com.vetcare.backend.employee.dto.AddCertificateRequest;
import com.vetcare.backend.employee.dto.CertificateResponse;
import com.vetcare.backend.employee.dto.EmployeeProfileResponse;
import com.vetcare.backend.employee.dto.UpdateEmployeeProfile;
import com.vetcare.backend.employee.model.EmployeeCertificateEntity;
import com.vetcare.backend.employee.model.EmployeeEntity;
import com.vetcare.backend.employee.repository.EmployeeCertificateRepository;
import com.vetcare.backend.employee.repository.EmployeeRepository;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeCertificateRepository certificateRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<EmployeeProfileResponse> getAllEmployees (){
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

        return employeeEntities.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getMyProfile(CustomUserDetails userDetails) {
        EmployeeEntity employee = getEmployeeByEmail(userDetails.getUsername());
        return mapToResponse(employee);
    }

    @Transactional
    public EmployeeProfileResponse updateMyProfile(UpdateEmployeeProfile request, CustomUserDetails userDetails) {
        EmployeeEntity employee = getEmployeeByEmail(userDetails.getUsername());

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

        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Transactional
    public EmployeeProfileResponse addCertificate(AddCertificateRequest request, CustomUserDetails userDetails) {
        EmployeeEntity employee = getEmployeeByEmail(userDetails.getUsername());

        String imagePath = fileStorageService.storeFile(request.image(), "employees/certificates");

        EmployeeCertificateEntity certificate = EmployeeCertificateEntity.builder()
                .imagePath(imagePath)
                .title(request.title())
                .displayOrder(request.displayOrder() != null ? request.displayOrder() : 0)
                .build();

        employee.addCertificate(certificate);
        employeeRepository.save(employee);

        return mapToResponse(employee);
    }

    @Transactional
    public void deleteCertificate(UUID certificateId, CustomUserDetails userDetails) {
        EmployeeEntity employee = getEmployeeByEmail(userDetails.getUsername());

        EmployeeCertificateEntity certificate = certificateRepository.findById(certificateId)
                .orElseThrow(CertificateNotFoundException::new);

        if (!certificate.getEmployee().getId().equals(employee.getId())) {
            throw new RuntimeException("You don't have permission to delete this certificate");
        }

        fileStorageService.deleteFile(certificate.getImagePath());
        employee.getCertificates().remove(certificate);
        certificateRepository.delete(certificate);
    }

    @Transactional
    public EmployeeProfileResponse createMyProfile(UpdateEmployeeProfile request, CustomUserDetails userDetails) {
        if (employeeRepository.existsByUser_Email(userDetails.getUsername())) {
            throw new IllegalStateException("Masz już utworzony profil pracownika.");
        }

        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
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
        return mapToResponse(savedEmployee);
    }


    private EmployeeEntity getEmployeeByEmail(String email) {
        return employeeRepository.findByUser_Email(email)
                .orElseThrow(InvalidOwnerException::new);
    }

    public EmployeeProfileResponse mapToResponse(EmployeeEntity entity) {
        List<CertificateResponse> certs = entity.getCertificates().stream()
                .map(c -> new CertificateResponse(c.getId(), c.getImagePath(), c.getTitle(), c.getDisplayOrder()))
                .toList();

        return new EmployeeProfileResponse(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDescription(),
                entity.getProfileImage(),
                certs
        );
    }
}