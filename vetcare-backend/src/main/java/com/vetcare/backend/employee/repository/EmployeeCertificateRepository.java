package com.vetcare.backend.employee.repository;

import com.vetcare.backend.employee.model.EmployeeCertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeCertificateRepository extends JpaRepository<EmployeeCertificateEntity, UUID> {
}