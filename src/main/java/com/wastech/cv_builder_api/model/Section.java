package com.wastech.cv_builder_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cv_id")
    private UUID cvId;

    @NotBlank
    @Column(name = "type")
    private String type;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Min(0)
    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "visible", columnDefinition = "boolean default true")
    private boolean visible = true;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private Cv cv;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionContent> contents;
}
