package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.SectionDto;
import com.wastech.cv_builder_api.dto.SectionRequestDto;
import com.wastech.cv_builder_api.exceptions.DuplicateResourceException;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.Section;
import com.wastech.cv_builder_api.repository.CVRepository;
import com.wastech.cv_builder_api.repository.SectionRepository;
import com.wastech.cv_builder_api.service.impl.SectionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private CVRepository cvRepository;

    @InjectMocks
    private SectionServiceImpl sectionService;

    private SectionRequestDto sectionRequestDto;
    private SectionDto sectionDto;
    private CV cv;
    private Section section;

    @BeforeEach
    public void setUp() {
        cv = new CV();
        cv.setId(UUID.randomUUID());

        sectionRequestDto = new SectionRequestDto();
        sectionRequestDto.setCvId(cv.getId());
        sectionRequestDto.setTitle("Test Section");
        sectionRequestDto.setType("TestType");

        sectionDto = new SectionDto();
        sectionDto.setId(UUID.randomUUID());
        sectionDto.setCvId(cv.getId());
        sectionDto.setTitle("Test Section");
        sectionDto.setType("TestType");

        section = Section.builder()
            .id(sectionDto.getId())
            .title(sectionDto.getTitle())
            .type(sectionDto.getType())
            .cv(cv)
            .build();
    }

    @Test
    public void testCreateSection_Success() {
        when(cvRepository.findById(any(UUID.class))).thenReturn(Optional.of(cv));
        when(sectionRepository.existsByCvIdAndTitleIgnoreCase(any(UUID.class), anyString())).thenReturn(false);
        when(sectionRepository.save(any(Section.class))).thenReturn(section);

        SectionDto result = sectionService.createSection(sectionRequestDto);

        assertNotNull(result);
        assertEquals(sectionRequestDto.getTitle(), result.getTitle());
        verify(sectionRepository, times(1)).save(any(Section.class));
    }

    @Test
    public void testCreateSection_CVNotFound() {
        when(cvRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sectionService.createSection(sectionRequestDto);
        });

        assertEquals("CV not found with id: " + sectionRequestDto.getCvId(), exception.getMessage());
    }

    @Test
    public void testCreateSection_DuplicateTitle() {
        when(cvRepository.findById(any(UUID.class))).thenReturn(Optional.of(cv));
        when(sectionRepository.existsByCvIdAndTitleIgnoreCase(any(UUID.class), anyString())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, () -> {
            sectionService.createSection(sectionRequestDto);
        });

        assertEquals("Section with title '" + sectionRequestDto.getTitle() + "' already exists for this CV", exception.getMessage());
    }

    @Test
    public void testUpdateSection_Success() {
        UUID sectionId = section.getId();

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));
        when(sectionRepository.save(any(Section.class))).thenReturn(section);

        SectionDto result = sectionService.updateSection(sectionId, sectionDto);

        assertNotNull(result);
        assertEquals(sectionDto.getTitle(), result.getTitle());
        verify(sectionRepository, times(1)).save(any(Section.class));
    }

    @Test
    public void testUpdateSection_NotFound() {
        UUID sectionId = UUID.randomUUID();

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sectionService.updateSection(sectionId, sectionDto);
        });

        assertEquals("Section not found with id: " + sectionId, exception.getMessage());
    }


    @Test
    public void testDeleteSection_Success() {
        UUID sectionId = section.getId();

        when(sectionRepository.existsById(sectionId)).thenReturn(true);

        sectionService.deleteSection(sectionId);

        verify(sectionRepository, times(1)).deleteById(sectionId);
    }

    @Test
    public void testDeleteSection_NotFound() {
        UUID sectionId = UUID.randomUUID();

        when(sectionRepository.existsById(sectionId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sectionService.deleteSection(sectionId);
        });

        assertEquals("Section not found with id: " + sectionId, exception.getMessage());
    }

    @Test
    public void testFindSectionById_Success() {
        UUID sectionId = section.getId();

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));

        SectionDto result = sectionService.findSectionById(sectionId);

        assertNotNull(result);
        assertEquals(section.getTitle(), result.getTitle());
    }

    @Test
    public void testFindSectionById_NotFound() {
        UUID sectionId = UUID.randomUUID();

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sectionService.findSectionById(sectionId);
        });

        assertEquals("Section not found with id: " + sectionId, exception.getMessage());
    }

    @Test
    public void testFindAllSectionsByCvId_Success() {
        UUID cvId = cv.getId();

        when(cvRepository.existsById(cvId)).thenReturn(true);
        when(sectionRepository.findByCvId(cvId)).thenReturn(Collections.singletonList(section));

        List<SectionDto> result = sectionService.findAllSectionsByCvId(cvId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(section.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testFindAllSectionsByCvId_CVNotFound() {
        UUID cvId = UUID.randomUUID();

        when(cvRepository.existsById(cvId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sectionService.findAllSectionsByCvId(cvId);
        });

        assertEquals("CV not found with id: " + cvId, exception.getMessage());

    }
}