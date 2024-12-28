package com.wastech.cv_builder_api.controller;

import com.wastech.cv_builder_api.dto.SectionDto;
import com.wastech.cv_builder_api.dto.SectionRequestDto;
import com.wastech.cv_builder_api.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sections")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SectionController {

    @Autowired
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<SectionDto> createSection(@Valid @RequestBody SectionRequestDto sectionRequestDto) {
        SectionDto createdSection = sectionService.createSection(sectionRequestDto);
        return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDto> getSectionById(@PathVariable UUID id) {
        SectionDto section = sectionService.findSectionById(id);
        return ResponseEntity.ok(section);
    }

    @GetMapping("/cv/{cvId}")
    public ResponseEntity<List<SectionDto>> getAllSectionsByCvId(@PathVariable UUID cvId) {
        List<SectionDto> sections = sectionService.findAllSectionsByCvId(cvId);
        return ResponseEntity.ok(sections);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDto> updateSection(
        @PathVariable UUID id,
        @Valid @RequestBody SectionDto sectionDto) {
        SectionDto updatedSection = sectionService.updateSection(id, sectionDto);
        return ResponseEntity.ok(updatedSection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }
}