package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CVRepository extends JpaRepository<CV, UUID>, JpaSpecificationExecutor<CV> {

    Page<CV> findAll(Specification<CV> spec, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CV c WHERE c.isDeleted = false")
    long countActiveCvs();

    Optional<CV> findByTitle(String title);

    // Count total active (non-deleted) CVs
    long countByIsDeletedFalse();

    // Count CVs by Language
    @Query("SELECT c.languageCode, COUNT(c) FROM CV c WHERE c.isDeleted = false GROUP BY c.languageCode")
    List<Object[]> countCVsByLanguage();

    // Count CVs by Status
    @Query("SELECT c.status, COUNT(c) FROM CV c WHERE c.isDeleted = false GROUP BY c.status")
    List<Object[]> countCVsByStatus();

    Optional<CV> findByTemplateId(UUID templateId);

    List<CV> findByUserOrderByCreatedAtDesc(User user);

    Page<CV> findByUserAndIsDeletedFalseOrderByCreatedAtDesc(User user, Pageable pageable);
    List<CVDTO> getCVsByUser(User user);
}
