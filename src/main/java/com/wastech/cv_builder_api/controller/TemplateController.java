package com.wastech.cv_builder_api.controller;

import com.wastech.cv_builder_api.dto.TemplateDto;
import com.wastech.cv_builder_api.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/public/templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @PostMapping
    public ResponseEntity<TemplateDto> createTemplate(@RequestBody TemplateDto templateDto) {
        TemplateDto createdTemplate = templateService.createTemplate(templateDto);
        return ResponseEntity.ok(createdTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateDto> updateTemplate(@PathVariable UUID id, @RequestBody TemplateDto templateDto) {
        TemplateDto updatedTemplate = templateService.updateTemplate(id, templateDto);
        return ResponseEntity.ok(updatedTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable UUID id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDto> getTemplateById(@PathVariable UUID id) {
        TemplateDto template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @GetMapping
    public ResponseEntity<Page<TemplateDto>> getAllTemplates(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TemplateDto> templatesPage = templateService.getAllTemplates(pageable);
        return ResponseEntity.ok(templatesPage);
    }
}