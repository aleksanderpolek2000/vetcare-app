package com.vetcare.backend.pet.model;

import com.vetcare.backend.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pets")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column
    private String breed;

    @Column(name = "date_of_birth", nullable = false)
    LocalDateTime dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    UserEntity owner;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at")
    @UpdateTimestamp
    @Builder.Default
    LocalDateTime modifiedAt = LocalDateTime.now();

}
