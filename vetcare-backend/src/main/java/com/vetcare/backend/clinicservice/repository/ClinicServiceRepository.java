package com.vetcare.backend.clinicservice.repository;

import com.vetcare.backend.clinicservice.model.ClinicServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClinicServiceRepository extends JpaRepository<ClinicServiceEntity, UUID>, JpaSpecificationExecutor<ClinicServiceEntity> {
    @Query("SELECT COALESCE(MAX(cs.displayOrder) + 1, 0) FROM ClinicServiceEntity cs")
    Integer getNextDisplayOrder();

    @Query("SELECT cs FROM ClinicServiceEntity cs WHERE cs.category.id = :categoryId ORDER BY cs.displayOrder ASC")
    List<ClinicServiceEntity> getAllByCategoryId(@Param("categoryId") UUID categoryId);

    Optional<ClinicServiceEntity> findById(UUID id);
}
