package com.vetcare.backend.clinicservice.Specifications;

import com.vetcare.backend.clinicservice.model.ClinicServiceEntity;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ClinicServiceSpecifications {

    public static Specification<ClinicServiceEntity> fetchCreatedBy() {
        return (root, query, criteriaBuilder) -> {
            if (!Long.class.equals(query.getResultType())) {
                root.fetch("createdBy", JoinType.INNER);
            }
            return null;
        };
    }

    public static Specification<ClinicServiceEntity> fetchCategory() {
        return (root, query, criteriaBuilder) -> {
            if (!Long.class.equals(query.getResultType())) {
                root.fetch("category", JoinType.INNER);
            }
            return null;
        };
    }

    public static Specification<ClinicServiceEntity> hasCategoryId(UUID categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }


}
