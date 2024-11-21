package com.wastech.cv_builder_api.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Cv {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(length = 1000)
    private String summary;

    @Enumerated(EnumType.STRING)
    private CvStatus status;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;


}
