package com.vetcare.backend.clinicservice.model;

import com.vetcare.backend.clinicservicecategory.model.ClinicServiceCategoryEntity;
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

    @Column(length = 1000)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ClinicServiceCategoryEntity category;

}
