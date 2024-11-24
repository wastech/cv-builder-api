package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.Cv;
import com.wastech.cv_builder_api.model.CvStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CvRepository extends JpaRepository<Cv, UUID> {

    @Query("SELECT c FROM Cv c WHERE " +
        "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
        "(:languageCode IS NULL OR c.languageCode = :languageCode) AND " +
        "(:status IS NULL OR c.status = :status) AND " +
        "(:createdAfter IS NULL OR c.createdAt >= :createdAfter) AND " +
        "(:createdBefore IS NULL OR c.createdAt <= :createdBefore) AND " +
        "(:isDeleted IS NULL OR c.isDeleted = :isDeleted)")
    Page<Cv> advancedSearch(
        @Param("title") String title,
        @Param("languageCode") String languageCode,
        @Param("status") String status,
        @Param("createdAfter") LocalDateTime createdAfter,
        @Param("createdBefore") LocalDateTime createdBefore,
        @Param("isDeleted") Boolean isDeleted,
        Pageable pageable);

    @Query("SELECT COUNT(c) FROM Cv c WHERE c.isDeleted = false")
    long countActiveCvs();

    @Query("SELECT c.languageCode, COUNT(c) FROM Cv c WHERE c.isDeleted = false GROUP BY c.languageCode")
    List<Object[]> countCvsByLanguage();

    @Query("SELECT c.status, COUNT(c) FROM Cv c WHERE c.isDeleted = false GROUP BY c.status")
    List<Object[]> countCvsByStatus();

    // Additional useful methods
    List<Cv> findByStatusAndIsDeletedFalse(CvStatus status);

    List<Cv> findByLanguageCodeAndIsDeletedFalse(String languageCode);

    @Query("SELECT c FROM Cv c WHERE c.isDeleted = false AND " +
        "c.updatedAt >= :lastModifiedDate ORDER BY c.updatedAt DESC")
    List<Cv> findRecentlyModified(@Param("lastModifiedDate") LocalDateTime lastModifiedDate);
}
