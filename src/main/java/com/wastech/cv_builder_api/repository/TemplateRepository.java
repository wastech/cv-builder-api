package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
    Optional<Template> findByName(String name);
    @Query("SELECT t FROM Template t WHERE (:active IS NULL OR t.active = :active)")
    Page<Template> findTemplates(@Param("active") Boolean active, Pageable pageable);
}
