package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.SectionContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SectionContentRepository extends JpaRepository<SectionContent, UUID> {
    List<SectionContent> findBySectionId(UUID sectionId);
    void deleteBySectionId(UUID sectionId);
}