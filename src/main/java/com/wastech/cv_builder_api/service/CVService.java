package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.dto.CVSearchCriteria;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CVService {
    /**
     * Create a new CV
     * @param cVDTO CVDTO to be created
     * @return Created CV
     */
    CVDTO createCV(CVDTO cVDTO);

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
     * Advanced search for CVs with multiple filtering options
     * @param searchCriteria Search criteria
     * @param pageable Pagination information
     * @return Page of CVs matching the search criteria
     */
    Page<CV> searchCVs(CVSearchCriteria searchCriteria, Pageable pageable);

    /**
     * Retrieve a CV by its ID
     * @param id CV identifier
     * @return Optional CV
     */
    Optional<CV> getCVById(UUID id);

    /**
     * Get CVs by status
     * @param status CV status
     * @return List of CVs with given status
     */
    List<CV> getCVsByStatus(CVStatus status);

    /**
     * Get CVs by language code
     * @param languageCode Language code
     * @return List of CVs in specified language
     */
    List<CV> getCVsByLanguage(String languageCode);

    /**
     * Get CV statistics
     * @return CVStatisticsDTO with various statistics
     */
    CVStatisticsDTO getCVStatistics();

    /**
     * Retrieve recently modified CVs
     * @param hoursAgo Number of hours to look back
     * @return List of recently modified CVs
     */
    List<CV> getRecentlyModifiedCVs(int hoursAgo);
}