package com.vetcare.backend.clinicservice.model;

import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
import com.vetcare.backend.clinicservicecategory.repository.ClinicServiceCategoryRepository;
import com.vetcare.backend.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clinic_services")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ClinicServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(name = "image_path")
    private String image;

    @Column(nullable = false)
    private String slug;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "display_order", unique = true)
    Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    UserEntity createdBy;

    @Column(nullable = false)
    @Builder.Default
    Boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ClinicServiceCategoryEntity category;

}
