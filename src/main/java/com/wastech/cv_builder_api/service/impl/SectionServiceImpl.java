package com.wastech.cv_builder_api.service.impl;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.dto.SectionDto;
import com.wastech.cv_builder_api.dto.SectionRequestDto;
import com.wastech.cv_builder_api.exceptions.DuplicateResourceException;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.Section;
import com.wastech.cv_builder_api.repository.CVRepository;
import com.wastech.cv_builder_api.repository.SectionRepository;
import com.wastech.cv_builder_api.service.SectionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    @Autowired
    private final SectionRepository sectionRepository;

    @Autowired
    private final CVRepository cvRepository;

    @Override
    @Transactional
    public SectionDto createSection(SectionRequestDto requestDto) {
        // Verify CV exists
        CV cv = cvRepository.findById(requestDto.getCvId())
            .orElseThrow(() -> new EntityNotFoundException("CV not found with id: " + requestDto.getCvId()));


        // Check if section title already exists for this CV
        if (sectionRepository.existsByCvIdAndTitleIgnoreCase(requestDto.getCvId(), requestDto.getTitle())) {
            throw new DuplicateResourceException("Section with title '" + requestDto.getTitle() + "' already exists for this CV");
        }

        Section section = Section.builder()
            .type(requestDto.getType())
            .title(requestDto.getTitle())
            .orderIndex(requestDto.getOrderIndex())
            .visible(requestDto.isVisible())
            .cv(cv)
            .build();

        Section savedSection = sectionRepository.save(section);
        return mapToDto(savedSection);
    }

    @Override
    @Transactional
    public SectionDto updateSection(UUID id, SectionDto sectionDto) {
        Section existingSection = sectionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + id));

        // Check for duplicate title only if title is being changed
        if (!existingSection.getTitle().equalsIgnoreCase(sectionDto.getTitle()) &&
            sectionRepository.existsByCvIdAndTitleIgnoreCase(
                existingSection.getCv().getId(),
                sectionDto.getTitle()
            )) {
            throw new DuplicateResourceException("Section with title '" + sectionDto.getTitle() + "' already exists for this CV");
        }


        // Verify CV exists if CV is being changed
        if (sectionDto.getCvId() != null && !existingSection.getCv().getId().equals(sectionDto.getCvId())) {
            CV newCv = cvRepository.findById(sectionDto.getCvId())
                .orElseThrow(() -> new EntityNotFoundException("CV not found with id: " + sectionDto.getCvId()));
            existingSection.setCv(newCv);
        }

        // Update fields
        existingSection.setType(sectionDto.getType());
        existingSection.setTitle(sectionDto.getTitle());
        existingSection.setOrderIndex(sectionDto.getOrderIndex());
        existingSection.setVisible(sectionDto.isVisible());

        Section updatedSection = sectionRepository.save(existingSection);
        return mapToDto(updatedSection);
    }

    @Override
    @Transactional
    public void deleteSection(UUID id) {
        if (!sectionRepository.existsById(id)) {
            throw new EntityNotFoundException("Section not found with id: " + id);
        }
        sectionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SectionDto findSectionById(UUID id) {
        Section section = sectionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + id));
        return mapToDto(section);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SectionDto> findAllSectionsByCvId(UUID cvId) {
        if (!cvRepository.existsById(cvId)) {
            throw new EntityNotFoundException("CV not found with id: " + cvId);
        }
        List<Section> sections = sectionRepository.findByCvId(cvId);
        return sections.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    // Helper methods for mapping between DTO and Entity
    private Section mapToEntity(SectionDto dto) {
        CV cv = cvRepository.findById(dto.getCvId())
            .orElseThrow(() -> new EntityNotFoundException("CV not found with id: " + dto.getCvId()));

        return Section.builder()
            .id(dto.getId())
            .type(dto.getType())
            .title(dto.getTitle())
            .orderIndex(dto.getOrderIndex())
            .visible(dto.isVisible())
            .cv(cv)
            .build();
    }

    private SectionDto mapToDto(Section entity) {
        return SectionDto.builder()
            .id(entity.getId())
            .type(entity.getType())
            .title(entity.getTitle())
            .orderIndex(entity.getOrderIndex())
            .visible(entity.isVisible())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .cvId(entity.getCv().getId())
            .build();
    }
}