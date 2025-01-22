package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.SectionDto;
import com.wastech.cv_builder_api.dto.SectionRequestDto;

import java.util.List;
import java.util.UUID;

public interface SectionService {
    SectionDto createSection(SectionRequestDto sectionRequestDto);
    SectionDto updateSection(UUID id, SectionDto sectionDto);
    void deleteSection(UUID id);
    SectionDto findSectionById(UUID id);
    List<SectionDto> findAllSectionsByCvId(UUID id);
}
