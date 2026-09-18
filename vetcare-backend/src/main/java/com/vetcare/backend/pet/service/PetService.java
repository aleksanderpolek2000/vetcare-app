package com.vetcare.backend.pet.service;

import com.vetcare.backend.common.dto.PageResponse;
import com.vetcare.backend.pet.Specifications.PetSpecifications;
import com.vetcare.backend.pet.dto.CreatePetRequest;
import com.vetcare.backend.pet.dto.PetResponse;
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
