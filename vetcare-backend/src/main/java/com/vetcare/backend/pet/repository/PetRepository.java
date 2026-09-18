package com.vetcare.backend.pet.repository;

import com.vetcare.backend.pet.model.PetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, UUID>, JpaSpecificationExecutor<PetEntity> {

    Page<PetEntity> findAllByOwnerId(UUID ownerId, Pageable pageable);

}
