package com.wastech.cv_builder_api.controller;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.service.CVService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
public class CVController {

    @Autowired
    private final CVService cvService;

    /**
     * Create a new CV
     * @param cvCreateDTO DTO containing CV creation details
     * @return Created CV with HTTP 201 Created status
     */
    @PostMapping
    public ResponseEntity<CVDTO> createCV(@Valid @RequestBody CVDTO cvCreateDTO) {
        CVDTO createdCVDTO = cvService.createCV(cvCreateDTO);
        return new ResponseEntity<>(createdCVDTO, HttpStatus.CREATED);
    }
}