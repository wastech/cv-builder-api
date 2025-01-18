package com.wastech.cv_builder_api.service.impl;

import com.wastech.cv_builder_api.dto.TemplateDto;
import com.wastech.cv_builder_api.exceptions.ResourceAlreadyExistsException;
import com.wastech.cv_builder_api.exceptions.ResourceNotFoundException;
import com.wastech.cv_builder_api.model.Template;
import com.wastech.cv_builder_api.repository.TemplateRepository;
import com.wastech.cv_builder_api.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public TemplateDto createTemplate(TemplateDto templateDto) {
        // Check if the template with the same name already exists
        Optional<Template> existingTemplate = templateRepository.findByName(templateDto.getName());
        if (existingTemplate.isPresent()) {
            throw new ResourceAlreadyExistsException("Template with name " + templateDto.getName() + " already exists");
        }

        Template template = mapToEntity(templateDto);
        Template newTemplate = templateRepository.save(template);
        return mapToDto(newTemplate);
    }
    @Override
    public TemplateDto updateTemplate(UUID id, TemplateDto templateDto) {
        Optional<Template> existingTemplate = templateRepository.findById(id);
        if (existingTemplate.isPresent()) {
            Template template = existingTemplate.get();
            if (templateDto.getName() != null) {
                template.setName(templateDto.getName());
            }
            if (templateDto.getDescription() != null) {
                template.setDescription(templateDto.getDescription());
            }
            if (templateDto.getDefaultStyles() != null) {
                template.setDefaultStyles(templateDto.getDefaultStyles());
            }
            if (templateDto.getLayoutConfig() != null) {
                template.setLayoutConfig(templateDto.getLayoutConfig());
            }
            template.setActive(templateDto.isActive());
            Template updatedTemplate = templateRepository.save(template);
            return mapToDto(updatedTemplate);
        } else {
            throw new ResourceNotFoundException("Template", "templateId", id);
        }
    }

    @Override
    public void deleteTemplate(UUID id) {
        templateRepository.deleteById(id);
    }

    @Override
    public TemplateDto getTemplateById(UUID id) {
        Template template = templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Template", "templateId",id ));
        return mapToDto(template);
    }

    @Override
    public Page<TemplateDto> getAllTemplates(Pageable pageable) {
        Page<Template> templatesPage = templateRepository.findAll(pageable);
        List<TemplateDto> templateDtos = templatesPage.getContent().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
        return new PageImpl<>(templateDtos, pageable, templatesPage.getTotalElements());
    }
    private TemplateDto mapToDto(Template template) {
        return new TemplateDto(
            template.getId(),
            template.getName(),
            template.getDescription(),
            template.getDefaultStyles(),
            template.getLayoutConfig(),
            template.isActive(),
            template.getCreatedAt(),
            template.getUpdatedAt()
        );
    }

    private Template mapToEntity(TemplateDto templateDto) {
        return new Template(
            templateDto.getId(),
            templateDto.getName(),
            templateDto.getDescription(),
            templateDto.getDefaultStyles(),
            templateDto.getLayoutConfig(),
            templateDto.isActive(),
            templateDto.getCreatedAt(),
            templateDto.getUpdatedAt(),
            null
        );
    }
}