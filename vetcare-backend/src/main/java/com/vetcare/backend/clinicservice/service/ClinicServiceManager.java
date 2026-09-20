package com.vetcare.backend.clinicservice.service;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.clinicservice.Specifications.ClinicServiceSpecifications;
import com.vetcare.backend.clinicservice.dto.ClinicServiceResponse;
import com.vetcare.backend.clinicservice.dto.CreateClinicService;
import com.vetcare.backend.clinicservice.dto.UpdateClinicService;
import com.vetcare.backend.clinicservice.model.ClinicServiceEntity;
import com.vetcare.backend.clinicservice.repository.ClinicServiceRepository;
import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
import com.vetcare.backend.clinicservicecategory.repository.ClinicServiceCategoryRepository;
import com.vetcare.backend.common.exception.InvalidOwnerException;
import com.vetcare.backend.common.exception.InvalidServiceCategoryException;
import com.vetcare.backend.common.exception.InvalidServiceException;
import com.vetcare.backend.common.storage.FileStorageService;
import com.vetcare.backend.common.util.SlugUtils;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicServiceManager {

    private final UserRepository userRepository;
    private final ClinicServiceCategoryRepository clinicServiceCategoryRepository;
    private final ClinicServiceRepository clinicServiceRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ClinicServiceResponse> getClinicServicesList(UUID categoryId) {

        Specification<ClinicServiceEntity> specification = Specification
                .where(ClinicServiceSpecifications.hasCategoryId(categoryId))
                .and(ClinicServiceSpecifications.fetchCategory())
                .and(ClinicServiceSpecifications.fetchCreatedBy());;

        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder");

        List<ClinicServiceEntity> serviceEntityList = clinicServiceRepository.findAll(specification, sort);

        return serviceEntityList.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClinicServiceResponse getClinicService(UUID id) {
        ClinicServiceEntity clinicService = clinicServiceRepository.findById(id).orElseThrow(InvalidServiceException::new);

        return mapToResponse(clinicService);
    }

    @Transactional
    public ClinicServiceResponse createClinicService(CreateClinicService clinicServiceRequest, CustomUserDetails userDetails) {
        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(InvalidOwnerException::new);
        ClinicServiceCategoryEntity categoryEntity = clinicServiceCategoryRepository.findById(clinicServiceRequest.categoryId()).orElseThrow(InvalidServiceCategoryException::new);

        String imagePath;
        if (clinicServiceRequest.image() != null && !clinicServiceRequest.image().isEmpty()) {
            imagePath = fileStorageService.storeFile(clinicServiceRequest.image(), "services");
        } else {
            imagePath = "empty";
        }

        ClinicServiceEntity serviceEntity = ClinicServiceEntity.builder()
                .name(clinicServiceRequest.name())
                .description(clinicServiceRequest.description())
                .image(imagePath)
                .slug(SlugUtils.toSlug(clinicServiceRequest.name()))
                .displayOrder(clinicServiceRepository.getNextDisplayOrder())
                .createdBy(user)
                .category(categoryEntity)
                .build();

        ClinicServiceEntity savedClinicService = clinicServiceRepository.save(serviceEntity);

        return mapToResponse(savedClinicService);
    }

    @Transactional
    public ClinicServiceResponse updateClinicService(UUID id, UpdateClinicService clinicServiceRequest, CustomUserDetails userDetails) {
        ClinicServiceCategoryEntity categoryEntity = clinicServiceCategoryRepository.findById(clinicServiceRequest.categoryId()).orElseThrow(InvalidServiceCategoryException::new);
        ClinicServiceEntity clinicService = clinicServiceRepository.findById(id).orElseThrow(InvalidServiceException::new);

        if (clinicServiceRequest.image() != null && !clinicServiceRequest.image().isEmpty()) {
            if (clinicService.getImage() != null && !"empty".equals(clinicService.getImage())) {
                fileStorageService.deleteFile(clinicService.getImage());
            }
            String newImagePath = fileStorageService.storeFile(clinicServiceRequest.image(), "services");
            clinicService.setImage(newImagePath);
        } else {
            if (clinicService.getImage() != null && !"empty".equals(clinicService.getImage())) {
                fileStorageService.deleteFile(clinicService.getImage());
            }
            clinicService.setImage("empty");
        }

        clinicService.setName(clinicServiceRequest.name());
        clinicService.setSlug(SlugUtils.toSlug(clinicServiceRequest.name()));
        clinicService.setDescription(clinicServiceRequest.description());
        clinicService.setDisplayOrder(clinicServiceRequest.displayOrder());
        clinicService.setActive(clinicServiceRequest.active());
        clinicService.setUpdatedAt(LocalDateTime.now());
        clinicService.setCategory(categoryEntity);

        return mapToResponse(clinicService);
    }

    @Transactional
    public void deleteClinicService(UUID id) {
        ClinicServiceEntity clinicService = clinicServiceRepository.findById(id).orElseThrow(InvalidServiceException::new);

        if (clinicService.getImage() != null) {
            fileStorageService.deleteFile(clinicService.getImage());
        }

        clinicServiceRepository.delete(clinicService);
    }


    public ClinicServiceResponse mapToResponse(ClinicServiceEntity serviceEntity) {
        return new ClinicServiceResponse(serviceEntity.getId(),
                serviceEntity.getName(),
                serviceEntity.getDescription(),
                serviceEntity.getImage(),
                serviceEntity.getSlug(),
                serviceEntity.getCreatedAt(),
                serviceEntity.getUpdatedAt(),
                serviceEntity.getDisplayOrder(),
                serviceEntity.getCreatedBy().getId(),
                serviceEntity.getActive(),
                serviceEntity.getCategory().getId());
    }
}
