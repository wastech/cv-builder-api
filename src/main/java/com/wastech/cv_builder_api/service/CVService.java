package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.dto.CVSearchCriteria;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;

import com.wastech.cv_builder_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CVService {
    /**
     * Create a new CV
     * @param cVDTO CVDTO to be created
     * @return Created CV
     */
    CVDTO createCV(CVDTO cVDTO, User user);


    Page<CVDTO> getUserCVs(User user, int page, int size);

    /**
     * Retrieve all CVs with advanced filtering, pagination, and sorting
     * @param title Optional title filter
     * @param languageCode Optional language code filter
     * @param status Optional status filter
     * @param createdAfter Optional creation date start filter
     * @param createdBefore Optional creation date end filter
     * @param isDeleted Optional deletion status filter
     * @param pageable Pagination and sorting parameters
     * @return Paginated and filtered list of CVs
     */
    Page<CV> getAllCVsWithFilters(
        String title,
        String languageCode,
        String status,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        Boolean isDeleted,
        Pageable pageable
    );

    /**
     * Update an existing CV
     * @param id CV identifier
     * @param updatedCV Updated CV details
     * @return Updated CV
     */
    CV updateCV(UUID id, CV updatedCV);

    /**
     * Soft delete a CV
     * @param id CV identifier
     */
    void deleteCV(UUID id);



    /**
     * Retrieve a CV by its ID
     * @param id CV identifier
     * @return Optional CV
     */
    Optional<CV> getCVById(UUID id);


    /**
     * Get CV statistics
     * @return CVStatisticsDTO with various statistics
     */
    CVStatisticsDTO getCVStatistics();


}