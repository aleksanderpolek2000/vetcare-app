package com.vetcare.backend.employee.repository;

import com.vetcare.backend.employee.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    Optional<EmployeeEntity> findByUser_Email(String email);
    boolean existsByUser_Email(String email);

    boolean existsByUser_Id(UUID userId);
}