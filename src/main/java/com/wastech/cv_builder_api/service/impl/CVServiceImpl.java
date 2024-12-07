package com.wastech.cv_builder_api.service.impl;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.dto.CVSearchCriteria;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;
import com.wastech.cv_builder_api.exceptions.APIException;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.repository.CVRepository;
import com.wastech.cv_builder_api.service.CVService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    @Transactional
    public CVDTO createCV(CVDTO cvDTO) {

        // Check if a CV with the same title already exists
        Optional<CV> existingCV = cvRepository.findByTitle(cvDTO.getTitle());
        if (existingCV.isPresent()) {
            throw new APIException("A CV with this " + cvDTO.getTitle() + " already exists.");
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
    public CV updateCV(UUID id, CV updatedCV) {
        return null;
    }

    @Override
    public void deleteCV(UUID id) {

    }

    @Override
    public Page<CV> searchCVs(CVSearchCriteria searchCriteria, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<CV> getCVById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<CV> getCVsByStatus(CVStatus status) {
        return null;
    }

    @Override
    public List<CV> getCVsByLanguage(String languageCode) {
        return null;
    }

    @Override
    public CVStatisticsDTO getCVStatistics() {
        return null;
    }

    @Override
    public List<CV> getRecentlyModifiedCVs(int hoursAgo) {
        return null;
    }
}
