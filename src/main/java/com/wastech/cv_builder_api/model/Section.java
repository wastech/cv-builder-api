package com.wastech.cv_builder_api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "section", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"title", "cv_id"}, name = "uk_section_title_cv")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY ,optional = false)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionContent> contents;
}
