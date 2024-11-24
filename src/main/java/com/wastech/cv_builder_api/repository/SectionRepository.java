package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
    @Query("SELECT s FROM Section s WHERE s.cv.id = :cvId " +
        "AND (:visible IS NULL OR s.visible = :visible) " +
        "AND (:type IS NULL OR s.type = :type)")
    Page<Section> findSectionsByCvId(
        @Param("cvId") UUID cvId,
        @Param("visible") Boolean visible,
        @Param("type") String type,
        Pageable pageable
    );
}
