package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.SectionContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SectionContentRepository extends JpaRepository<SectionContent, UUID> {

    // List all content items in a section with optional filters for "current" and sorting by "orderIndex"
    @Query("SELECT sc FROM SectionContent sc WHERE sc.section.id = :sectionId " +
        "AND (:current IS NULL OR sc.current = :current) " +
        "ORDER BY sc.orderIndex ASC")
    Page<SectionContent> findAllBySectionId(
        @Param("sectionId") UUID sectionId,
        @Param("current") Boolean current,
        Pageable pageable);

    // Retrieve a specific content item
    Optional<SectionContent> findByIdAndSectionId(UUID contentId, UUID sectionId);

    // Update content item order
    @Query("UPDATE SectionContent sc SET sc.orderIndex = :orderIndex WHERE sc.id = :contentId")
    void updateOrderIndex(@Param("contentId") UUID contentId, @Param("orderIndex") int orderIndex);

    // Delete content by ID and section
    void deleteByIdAndSectionId(UUID contentId, UUID sectionId);
}
