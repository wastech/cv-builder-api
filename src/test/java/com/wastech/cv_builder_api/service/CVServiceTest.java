package com.wastech.cv_builder_api.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;
import com.wastech.cv_builder_api.exceptions.APIException;
import com.wastech.cv_builder_api.exceptions.ResourceNotFoundException;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.CVStatus;
import com.wastech.cv_builder_api.model.Template;
import com.wastech.cv_builder_api.model.User;
import com.wastech.cv_builder_api.repository.CVRepository;
import com.wastech.cv_builder_api.repository.TemplateRepository;
import com.wastech.cv_builder_api.service.impl.CVServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class CVServiceTest {

    @Mock
    private CVRepository cvRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CVServiceImpl cvService;

    private CVDTO cvDTO;
    private User user;
    private CV cv;
    private Template template;

    @BeforeEach
    public void setUp() {
        cvDTO = new CVDTO();
        cvDTO.setTitle("Test CV");
        cvDTO.setTemplateId(UUID.fromString("0ac3c46e-a619-4fd9-b961-7ea6be162eb3"));

        user = new User();
        user.setUserId(UUID.randomUUID());

        cv = new CV();
        cv.setId(UUID.randomUUID());
        cv.setTitle("Test CV");

        template = new Template();
        template.setId(UUID.fromString("0ac3c46e-a619-4fd9-b961-7ea6be162eb3"));
    }

    @Test
    public void testCreateCV_Success() {
        when(cvRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findById(any(UUID.class))).thenReturn(Optional.of(template));
        when(modelMapper.map(any(CVDTO.class), eq(CV.class))).thenReturn(cv);
        when(cvRepository.save(any(CV.class))).thenReturn(cv);
        when(modelMapper.map(any(CV.class), eq(CVDTO.class))).thenReturn(cvDTO);

        CVDTO result = cvService.createCV(cvDTO, user);

        assertNotNull(result);
        assertEquals(cvDTO.getTitle(), result.getTitle());
        verify(cvRepository, times(1)).save(any(CV.class));
    }

    @Test
    public void testCreateCV_AlreadyExists() {
        when(cvRepository.findByTitle(anyString())).thenReturn(Optional.of(cv));

        APIException exception = assertThrows(APIException.class, () -> {
            cvService.createCV(cvDTO, user);
        });

        assertEquals("A CV with this " + cvDTO.getTitle() + " already exists.", exception.getMessage());
    }

    @Test
    public void testCreateCV_TemplateNotFound() {
        when(cvRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cvService.createCV(cvDTO, user);
        });

        assertEquals("Template not found with cvDTO.getTemplateId(): " + cvDTO.getTemplateId(), exception.getMessage());
    }

    @Test
    public void testGetUserCVs_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CV> cvPage = new PageImpl<>(Collections.singletonList(cv));
        when(cvRepository.findByUserAndIsDeletedFalseOrderByCreatedAtDesc(any(User.class), any(Pageable.class))).thenReturn(cvPage);
        when(modelMapper.map(any(CV.class), eq(CVDTO.class))).thenReturn(cvDTO);

        Page<CVDTO> result = cvService.getUserCVs(user, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(cvDTO.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void testGetAllCVsWithFilters_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CV> cvPage = new PageImpl<>(Collections.singletonList(cv));
        when(cvRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(cvPage);

        Page<CV> result = cvService.getAllCVsWithFilters(
            "Test CV",
            "en",
            "active",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now(),
            false,
            pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(cv.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    public void testUpdateCV_Success() {
        UUID cvId = cv.getId();
        CV updatedCV = new CV();
        updatedCV.setTitle("Updated CV");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));
        when(cvRepository.findByTitle(updatedCV.getTitle())).thenReturn(Optional.empty());
        when(cvRepository.save(any(CV.class))).thenReturn(cv);

        CV result = cvService.updateCV(cvId, updatedCV);

        assertNotNull(result);
        assertEquals(updatedCV.getTitle(), result.getTitle());
        verify(cvRepository, times(1)).save(any(CV.class));
    }

    @Test
    public void testUpdateCV_TitleAlreadyExists() {
        UUID cvId = cv.getId();
        CV updatedCV = new CV();
        updatedCV.setTitle("Updated CV");

        CV anotherCV = new CV();
        anotherCV.setId(UUID.randomUUID());
        anotherCV.setTitle("Updated CV");

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));
        when(cvRepository.findByTitle(updatedCV.getTitle())).thenReturn(Optional.of(anotherCV));

        APIException exception = assertThrows(APIException.class, () -> {
            cvService.updateCV(cvId, updatedCV);
        });

        assertEquals("A CV with title '" + updatedCV.getTitle() + "' already exists.", exception.getMessage());
    }

    @Test
    public void testUpdateCV_NotFound() {
        UUID cvId = UUID.randomUUID();
        CV updatedCV = new CV();

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            cvService.updateCV(cvId, updatedCV);
        });

        assertEquals("CV not found with id: " + cvId, exception.getMessage());
    }

    @Test
    public void testDeleteCV_Success() {
        UUID cvId = cv.getId();

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        cvService.deleteCV(cvId);

        assertTrue(cv.isDeleted());
        verify(cvRepository, times(1)).save(any(CV.class));
    }

    @Test
    public void testDeleteCV_NotFound() {
        UUID cvId = UUID.randomUUID();

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            cvService.deleteCV(cvId);
        });

        assertEquals("CV not found with id: " + cvId, exception.getMessage());
    }

    @Test
    public void testGetCVById_Success() {
        UUID cvId = cv.getId();

        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cv));

        Optional<CV> result = cvService.getCVById(cvId);

        assertTrue(result.isPresent());
        assertEquals(cv.getTitle(), result.get().getTitle());
    }

    @Test
    public void testGetCVById_NotFound() {
        UUID cvId = UUID.randomUUID();

        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        Optional<CV> result = cvService.getCVById(cvId);

        assertFalse(result.isPresent());
    }


}