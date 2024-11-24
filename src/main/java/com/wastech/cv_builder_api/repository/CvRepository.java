package com.wastech.cv_builder_api.repository;

import com.wastech.cv_builder_api.model.Cv;
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
        "(:createdAfter IS NULL OR c.createdAt >= :createdAfter) AND " +
        "(:createdBefore IS NULL OR c.createdAt <= :createdBefore)")
    List<Cv> advancedSearch(
        @Param("title") String title,
        @Param("languageCode") String languageCode,
        @Param("createdAfter") LocalDateTime createdAfter,
        @Param("createdBefore") LocalDateTime createdBefore);

    @Query("SELECT COUNT(c) FROM Cv c")
    long countAllCvs();

    @Query("SELECT c.languageCode, COUNT(c) FROM Cv c GROUP BY c.languageCode")
    List<Object[]> countCvsByLanguage();

    @Query("SELECT c.status, COUNT(c) FROM Cv c GROUP BY c.status")
    List<Object[]> countCvsByStatus();
}
