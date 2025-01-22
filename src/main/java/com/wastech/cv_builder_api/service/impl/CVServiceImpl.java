package com.wastech.cv_builder_api.service.impl;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.dto.CVSearchCriteria;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;
import com.wastech.cv_builder_api.exceptions.APIException;
import com.wastech.cv_builder_api.exceptions.ResourceNotFoundException;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.model.Template;
import com.wastech.cv_builder_api.repository.CVRepository;
import com.wastech.cv_builder_api.repository.TemplateRepository;
import com.wastech.cv_builder_api.service.CVService;
import com.wastech.cv_builder_api.util.CVSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CVServiceImpl implements CVService {

    @Autowired
    private final CVRepository cvRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final TemplateRepository templateRepository;

    @Override
    @Transactional
    public CVDTO createCV(CVDTO cvDTO) {

//         Check if a CV with the same title already exists
        Optional<CV> existingCV = cvRepository.findByTitle(cvDTO.getTitle());
        if (existingCV.isPresent()) {
            throw new APIException("A CV with this " + cvDTO.getTitle() + " already exists.");
        }

        // Check if the template ID exists in the Template repository
        Optional<Template> templateOptional = templateRepository.findById(cvDTO.getTemplateId());
        if (templateOptional.isEmpty()) {
            throw new ResourceNotFoundException("Template " , "cvDTO.getTemplateId() ", cvDTO.getTemplateId());
        }

        // Map DTO to entity
        CV cv = modelMapper.map(cvDTO, CV.class);

        // Set default status if not provided
        if (cv.getStatus() == null) {
            cv.setStatus(CVStatus.DRAFT);
        }

        cv.setCreatedAt(LocalDateTime.now());
        cv.setUpdatedAt(LocalDateTime.now());

        // Save the entity
        CV savedCV = cvRepository.save(cv);

        // Map saved entity back to DTO
        return modelMapper.map(savedCV, CVDTO.class);
    }

    @Override
    public Page<CV> getAllCVsWithFilters(
        String title,
        String languageCode,
        String status,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        Boolean isDeleted,
        Pageable pageable
    ) {
        Specification<CV> spec = CVSpecification.filterCVs(
            title,
            languageCode,
            status,
            createdAfter,
            createdBefore,
            isDeleted
        );

        return cvRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public CV updateCV(UUID id, CV updatedCV) {
        // Find the existing CV
        CV existingCV = cvRepository.findById(id)
            .orElseThrow(() -> new APIException("CV not found with id: " + id));

        // Update allowed fields
        if (updatedCV.getTitle() != null) {
            // Check if the new title is already in use by another CV
            Optional<CV> cvWithSameTitle = cvRepository.findByTitle(updatedCV.getTitle());
            if (cvWithSameTitle.isPresent() && !cvWithSameTitle.get().getId().equals(id)) {
                throw new APIException("A CV with title '" + updatedCV.getTitle() + "' already exists.");
            }
            existingCV.setTitle(updatedCV.getTitle());
        }

        if (updatedCV.getSummary() != null) {
            existingCV.setSummary(updatedCV.getSummary());
        }

        if (updatedCV.getStatus() != null) {
            existingCV.setStatus(updatedCV.getStatus());
        }

        if (updatedCV.getLanguageCode() != null) {
            existingCV.setLanguageCode(updatedCV.getLanguageCode());
        }

        // Explicitly set the updated at timestamp
        existingCV.setUpdatedAt(LocalDateTime.now());

        // Save and return the updated CV
        return cvRepository.save(existingCV);
    }

    @Override
    @Transactional
    public void deleteCV(UUID id) {
        // Find the CV first
        CV cv = cvRepository.findById(id)
            .orElseThrow(() -> new APIException("CV not found with id: " + id));

        // Soft delete - set isDeleted to true
        cv.setDeleted(true);
        cv.setUpdatedAt(LocalDateTime.now());
        cvRepository.save(cv);
    }

    @Override
    public Optional<CV> getCVById(UUID id) {
        return cvRepository.findById(id);
    }


    @Override
    public CVStatisticsDTO getCVStatistics() {
        // Create a new statistics DTO
        CVStatisticsDTO statistics = new CVStatisticsDTO();

        // Total active CVs (not deleted)
        long totalActiveCVs = cvRepository.countByIsDeletedFalse();
        statistics.setTotalActiveCVs(totalActiveCVs);

        // Count CVs by Language
        List<Object[]> languageCounts = cvRepository.countCVsByLanguage();
        languageCounts.forEach(result -> {
            String language = (String) result[0];
            Long count = (Long) result[1];
            statistics.addLanguageCount(language, count);
        });

        // Count CVs by Status - modify to convert enum to string
        List<Object[]> statusCounts = cvRepository.countCVsByStatus();
        statusCounts.forEach(result -> {
            CVStatus status = (CVStatus) result[0];
            Long count = (Long) result[1];
            statistics.addStatusCount(status.name(), count);
        });

        return statistics;
    }
}
