package com.vetcare.backend.pet.Specifications;

import com.vetcare.backend.common.exception.InvalidOwnerException;
import com.vetcare.backend.pet.model.PetEntity;
import jakarta.persistence.criteria.JoinType;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class PetSpecifications {

    public static Specification<@NonNull PetEntity> hasOwnerId(UUID id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                throw new InvalidOwnerException("Problem z identyfikatorem użytkownika!");
            }

            return criteriaBuilder.equal(root.get("owner").get("id"), id);
        };
    }

    public static Specification<@NonNull PetEntity> fetchOwner() {
        return (root, query, criteriaBuilder) -> {
            if (!Long.class.equals(query.getResultType())) {
                root.fetch("owner", JoinType.INNER);
            }
            return null;
        };
    }
}
