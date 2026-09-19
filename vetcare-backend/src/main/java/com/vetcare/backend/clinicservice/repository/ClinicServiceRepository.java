package com.vetcare.backend.clinicservice.repository;

import com.vetcare.backend.clinicservice.model.ClinicServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClinicServiceRepository extends JpaRepository<ClinicServiceEntity, UUID>, JpaSpecificationExecutor<ClinicServiceEntity> {
}
