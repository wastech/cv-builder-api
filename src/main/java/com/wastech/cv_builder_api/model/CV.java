package com.wastech.cv_builder_api.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
//import java.util.List;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "cv",  uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "title",unique = true)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Enumerated(EnumType.STRING)
    private CVStatus status;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "is_deleted", nullable = false )
    private boolean isDeleted = false;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    @ManyToOne
    @JoinColumn(name = "template_id" , nullable = false)
    private Template template;


}
