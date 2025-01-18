package com.wastech.cv_builder_api.service.impl;

import com.wastech.cv_builder_api.dto.SectionContentDto;
import com.wastech.cv_builder_api.model.Section;
import com.wastech.cv_builder_api.model.SectionContent;
import com.wastech.cv_builder_api.repository.SectionContentRepository;
import com.wastech.cv_builder_api.repository.SectionRepository;
import com.wastech.cv_builder_api.service.SectionContentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SectionContentServiceImpl implements SectionContentService {

    @Autowired
    private SectionContentRepository sectionContentRepository;

    @Autowired
    private SectionRepository sectionRepository;


    /**
     * Convert SectionContent entity to DTO
     */
    private SectionContentDto convertToDto(SectionContent sectionContent) {
        SectionContentDto dto = new SectionContentDto();
        BeanUtils.copyProperties(sectionContent, dto);
        // Fixed: Get the section ID from the section relationship
        dto.setSectionId(sectionContent.getSection().getId());
        return dto;
    }

    /**
     * Convert SectionContentDto to entity
     */
    private SectionContent convertToEntity(SectionContentDto dto) {
        SectionContent entity = new SectionContent();
        BeanUtils.copyProperties(dto, entity);

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
            entity.setSection(section);
        }

        return entity;
    }

    @Override
    @Transactional
    public SectionContentDto createSectionContent(SectionContentDto sectionContentDto) {
        // Validate that sectionId is not null
        if (sectionContentDto.getSectionId() == null) {
            throw new IllegalArgumentException("Section ID cannot be null");
        }

        SectionContent sectionContent = convertToEntity(sectionContentDto);
        SectionContent savedContent = sectionContentRepository.save(sectionContent);
        return convertToDto(savedContent);
    }

    @Override
    @Transactional
    public SectionContentDto updateSectionContent(UUID id, SectionContentDto sectionContentDto) {
        SectionContent existingContent = sectionContentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Section content not found"));

        // Update the content type if provided
        if (sectionContentDto.getContentType() != null) {
            existingContent.setContentType(sectionContentDto.getContentType());
        }

        // Update the section if sectionId is provided
        if (sectionContentDto.getSectionId() != null &&
            (existingContent.getSection() == null || !existingContent.getSection().getId().equals(sectionContentDto.getSectionId()))) {
            Section section = sectionRepository.findById(sectionContentDto.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
            existingContent.setSection(section);
        }

        // Update other fields if provided
        if (sectionContentDto.getContent() != null) {
            existingContent.setContent(sectionContentDto.getContent());
        }
            existingContent.setOrderIndex(sectionContentDto.getOrderIndex());
        if (sectionContentDto.getStartDate() != null) {
            existingContent.setStartDate(sectionContentDto.getStartDate());
        }
        if (sectionContentDto.getEndDate() != null) {
            existingContent.setEndDate(sectionContentDto.getEndDate());
        }



        // Update the updatedAt field
        existingContent.setUpdatedAt(LocalDateTime.now());

        SectionContent updatedContent = sectionContentRepository.save(existingContent);
        return convertToDto(updatedContent);
    }
    @Override
    public SectionContentDto getSectionContentById(UUID id) {
        SectionContent content = sectionContentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Section content not found"));
        return convertToDto(content);
    }

    @Override
    public List<SectionContentDto> getAllSectionContents() {
        return sectionContentRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<SectionContentDto> getSectionContentsBySection(UUID sectionId) {
        return sectionContentRepository.findBySectionId(sectionId).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSectionContent(UUID id) {
        if (!sectionContentRepository.existsById(id)) {
            throw new EntityNotFoundException("Section content not found");
        }
        sectionContentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllSectionContentsBySection(UUID sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new EntityNotFoundException("Section not found");
        }
        sectionContentRepository.deleteBySectionId(sectionId);
    }
}