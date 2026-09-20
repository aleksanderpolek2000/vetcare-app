package com.vetcare.backend.clinicservicecategory.model;

import com.vetcare.backend.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinic_services_category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClinicServiceCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(length = 1000)
    String description;

    @Column(nullable = false)
    String slug;
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "display_order", unique = true)
    Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    UserEntity createdBy;

    @Column(nullable = false)
    @Builder.Default
    Boolean active = false;

}
