package com.vetcare.backend.pet.service;

import com.vetcare.backend.auth.model.CustomUserDetails;
import com.vetcare.backend.common.dto.PageResponse;
import com.vetcare.backend.common.exception.InvalidPetException;
import com.vetcare.backend.pet.Specifications.PetSpecifications;
import com.vetcare.backend.pet.dto.CreatePetRequest;
import com.vetcare.backend.pet.dto.PetResponse;
import com.vetcare.backend.pet.dto.UpdatePetRequest;
import com.vetcare.backend.pet.model.PetEntity;
import com.vetcare.backend.pet.repository.PetRepository;
import com.vetcare.backend.user.model.UserEntity;
import com.vetcare.backend.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Transactional
    public PetResponse createPet(CreatePetRequest createPetRequest, String email) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        PetEntity petEntity = PetEntity.builder()
                .name(createPetRequest.name())
                .species(createPetRequest.species())
                .breed(createPetRequest.breed())
                .dateOfBirth(createPetRequest.dateOfBirth())
                .owner(user)
                .build();

        PetEntity savedPet = petRepository.save(petEntity);

        return mapToResponse(savedPet);
    }

    @Transactional(readOnly = true)
    public PageResponse<PetResponse> getUserPets(String email, Pageable pageable) {

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        Specification<@NonNull PetEntity> specification = Specification
                .where(PetSpecifications.fetchOwner())
                .and(PetSpecifications.hasOwnerId(user.getId()));

        Page<@NonNull PetEntity> petPage = petRepository.findAll(specification, pageable);
        Page<@NonNull PetResponse> mappedPetPage = petPage.map(this::mapToResponse);

        return PageResponse.from(mappedPetPage);
    }

    @Transactional
    public PetResponse updatePet(UUID id, UpdatePetRequest updatePetRequest, CustomUserDetails userDetails) {

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        PetEntity pet = petRepository.findByIdAndOwnerId(id, user.getId()).orElseThrow(() -> new InvalidPetException("Nie znaleziono zwierzęcia"));

        pet.setName(updatePetRequest.name());
        pet.setSpecies(updatePetRequest.species());
        pet.setBreed(updatePetRequest.breed());
        pet.setDateOfBirth(updatePetRequest.dateOfBirth());
        pet.setModifiedAt(LocalDateTime.now());

        return mapToResponse(pet);
    }

    @Transactional
    public void deletePet(UUID id, CustomUserDetails userDetails) {

        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        PetEntity pet = petRepository.findByIdAndOwnerId(id, user.getId()).orElseThrow(() -> new InvalidPetException("Nie znaleziono zwierzęcia"));

        petRepository.delete(pet);
    }

    public PetResponse mapToResponse(PetEntity petEntity) {
        return new PetResponse(petEntity.getId(),
                petEntity.getName(),
                petEntity.getSpecies(),
                petEntity.getBreed(),
                petEntity.getDateOfBirth(),
                petEntity.getOwner().getId(),
                petEntity.getCreatedAt(),
                petEntity.getModifiedAt());
    }

}
