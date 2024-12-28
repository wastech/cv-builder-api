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
    List<Section> findByCvId(UUID cvId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Section s WHERE s.cv.id = :cvId AND LOWER(s.title) = LOWER(:title)")
    boolean existsByCvIdAndTitleIgnoreCase(@Param("cvId") UUID cvId, @Param("title") String title);

}
