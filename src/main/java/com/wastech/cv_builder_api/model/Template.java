package com.wastech.cv_builder_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "template")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "default_styles", columnDefinition = "jsonb")
    private String defaultStyles;

    @Column(name = "layout_config", columnDefinition = "jsonb")
    private String layoutConfig;

    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "template")
    private List<Cv> cvs;
}
