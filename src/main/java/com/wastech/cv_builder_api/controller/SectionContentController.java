package com.wastech.cv_builder_api.controller;

import com.wastech.cv_builder_api.dto.SectionContentDto;
import com.wastech.cv_builder_api.service.SectionContentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "*")
public class SectionContentController {

    @Autowired
    private SectionContentService sectionContentService;

    @PostMapping("/public/section-contents")
    public ResponseEntity<SectionContentDto> createSectionContent(
        @Valid @RequestBody SectionContentDto sectionContentDto) {
        SectionContentDto createdContent = sectionContentService.createSectionContent(sectionContentDto);
        return new ResponseEntity<>(createdContent, HttpStatus.CREATED);
    }

    @PutMapping("/public/section-contents/{id}")
    public ResponseEntity<SectionContentDto> updateSectionContent(
        @PathVariable UUID id,
        @Valid @RequestBody SectionContentDto sectionContentDto) {
        SectionContentDto updatedContent = sectionContentService.updateSectionContent(id, sectionContentDto);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/public/section-contents/{id}")
    public ResponseEntity<SectionContentDto> getSectionContent(@PathVariable UUID id) {
        SectionContentDto content = sectionContentService.getSectionContentById(id);
        return ResponseEntity.ok(content);
    }

    @GetMapping("/public/section-contents")
    public ResponseEntity<List<SectionContentDto>> getAllSectionContents() {
        List<SectionContentDto> contents = sectionContentService.getAllSectionContents();
        return ResponseEntity.ok(contents);
    }

    @GetMapping("/public/section-contents/section/{sectionId}")
    public ResponseEntity<List<SectionContentDto>> getSectionContentsBySection(
        @PathVariable UUID sectionId) {
        List<SectionContentDto> contents = sectionContentService.getSectionContentsBySection(sectionId);
        return ResponseEntity.ok(contents);
    }

    @DeleteMapping("/public/section-contents/{id}")
    public ResponseEntity<Void> deleteSectionContent(@PathVariable UUID id) {
        sectionContentService.deleteSectionContent(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/public/section-contents/section/{sectionId}")
    public ResponseEntity<Void> deleteAllSectionContentsBySection(
        @PathVariable UUID sectionId) {
        sectionContentService.deleteAllSectionContentsBySection(sectionId);
        return ResponseEntity.noContent().build();
    }
}