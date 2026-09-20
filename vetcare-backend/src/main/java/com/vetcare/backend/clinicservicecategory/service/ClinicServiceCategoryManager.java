package com.vetcare.backend.clinicservicecategory.service;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.clinicservicecategory.Specifications.ClinicServiceCategorySpecifications;
import com.vetcare.backend.clinicservicecategory.dto.ClinicServiceCategoryResponse;
import com.vetcare.backend.clinicservicecategory.dto.CreateClinicServiceCategory;
import com.vetcare.backend.clinicservicecategory.dto.UpdateClinicServiceCategory;
import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
import com.vetcare.backend.clinicservicecategory.repository.ClinicServiceCategoryRepository;
import com.vetcare.backend.common.exception.InvalidOwnerException;
import com.vetcare.backend.common.exception.InvalidServiceCategoryException;
import com.vetcare.backend.common.util.SlugUtils;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClinicServiceCategoryManager {

    private final ClinicServiceCategoryRepository clinicServiceCategoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ClinicServiceCategoryResponse> getClinicServiceCategories() {

        Specification<ClinicServiceCategoryEntity> specification = Specification.where(ClinicServiceCategorySpecifications.fetchCreatedBy());

        List<ClinicServiceCategoryEntity> categoryEntityList = clinicServiceCategoryRepository.findAll(specification);

        return categoryEntityList.stream().map(this::mapToResponse).toList();
    }

    @Transactional
    public ClinicServiceCategoryResponse createClinicServiceCategory(CreateClinicServiceCategory categoryRequest, CustomUserDetails userDetails) {

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(InvalidOwnerException::new);

        ClinicServiceCategoryEntity categoryEntity = ClinicServiceCategoryEntity.builder()
                .name(categoryRequest.name())
                .description(categoryRequest.description())
                .slug(SlugUtils.toSlug(categoryRequest.name()))
                .displayOrder(clinicServiceCategoryRepository.getNextDisplayOrder())
                .createdBy(user)
                .build();

        ClinicServiceCategoryEntity savedCategory = clinicServiceCategoryRepository.save(categoryEntity);

        return mapToResponse(savedCategory);
    }

    @Transactional
    public ClinicServiceCategoryResponse updateClinicServiceCategory(UUID id, UpdateClinicServiceCategory categoryRequest, CustomUserDetails userDetails) {

        ClinicServiceCategoryEntity categoryEntity = clinicServiceCategoryRepository.findById(id).orElseThrow(InvalidServiceCategoryException::new);

        categoryEntity.setName(categoryRequest.name());
        categoryEntity.setDescription(categoryRequest.description());
        categoryEntity.setDisplayOrder(categoryRequest.displayOrder());
        categoryEntity.setActive(categoryRequest.active());
        categoryEntity.setUpdatedAt(LocalDateTime.now());
        categoryEntity.setSlug(SlugUtils.toSlug(categoryRequest.name()));

        return mapToResponse(categoryEntity);
    }

    @Transactional
    public void deleteClinicServiceCategory(UUID id) {
        ClinicServiceCategoryEntity categoryEntity = clinicServiceCategoryRepository.findById(id).orElseThrow(InvalidServiceCategoryException::new);

        clinicServiceCategoryRepository.delete(categoryEntity);
    }

    public ClinicServiceCategoryResponse mapToResponse(ClinicServiceCategoryEntity categoryEntity) {
        return new ClinicServiceCategoryResponse(categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getDescription(),
                categoryEntity.getSlug(),
                categoryEntity.getCreatedAt(),
                categoryEntity.getUpdatedAt(),
                categoryEntity.getDisplayOrder(),
                categoryEntity.getCreatedBy().getId(),
                categoryEntity.getActive());
    }

}
