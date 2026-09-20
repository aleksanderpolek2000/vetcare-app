package com.vetcare.backend.clinicservicecategory.Specifications;

import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ClinicServiceCategorySpecifications {

    public static Specification<ClinicServiceCategoryEntity> fetchCreatedBy() {
        return (root, query, criteriaBuilder) -> {
            if (!Long.class.equals(query.getResultType())) {
                root.fetch("createdBy", JoinType.INNER);
            }
            return null;
        };
    }


}
