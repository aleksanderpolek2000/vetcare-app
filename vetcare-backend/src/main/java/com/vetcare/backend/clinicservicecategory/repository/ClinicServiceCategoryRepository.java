package com.vetcare.backend.clinicservicecategory.repository;

import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicServiceCategoryRepository extends JpaRepository<ClinicServiceCategoryEntity, UUID>, JpaSpecificationExecutor<ClinicServiceCategoryEntity> {
    @Query("SELECT COALESCE(MAX(csc.displayOrder) + 1, 0) FROM ClinicServiceCategoryEntity csc")
    Integer getNextDisplayOrder();
}
