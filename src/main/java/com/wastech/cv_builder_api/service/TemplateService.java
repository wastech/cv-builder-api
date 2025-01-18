package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.TemplateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TemplateService {
    TemplateDto createTemplate(TemplateDto templateDto);
    TemplateDto updateTemplate(UUID id, TemplateDto templateDto);
    void deleteTemplate(UUID id);
    TemplateDto getTemplateById(UUID id);
    Page<TemplateDto> getAllTemplates(Pageable pageable);}