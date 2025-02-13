package com.wastech.cv_builder_api.controller;

import com.wastech.cv_builder_api.dto.CVDTO;
import com.wastech.cv_builder_api.dto.CVSearchCriteria;
import com.wastech.cv_builder_api.dto.CVStatisticsDTO;
import com.wastech.cv_builder_api.model.CV;
import com.wastech.cv_builder_api.model.User;
import com.wastech.cv_builder_api.service.CVService;

import com.wastech.cv_builder_api.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CVController {

    @Autowired
    private final CVService cvService;


    @Autowired
    AuthUtil authUtil;

  @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/cv")
    public ResponseEntity<CVDTO> createCV(@Valid @RequestBody CVDTO cvCreateDTO) {

        User user = authUtil.loggedInUser();
        System.out.print("kkkkk"+ user);
        CVDTO createdCVDTO = cvService.createCV(cvCreateDTO,user);
        return new ResponseEntity<>(createdCVDTO, HttpStatus.CREATED);
    }

    @GetMapping("/user/cvs")
    public ResponseEntity<Page<CVDTO>> getUserCVs(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size) {
        User user = authUtil.loggedInUser();

        Page<CVDTO> cvs = cvService.getUserCVs(user, page, size);
        return ResponseEntity.ok(cvs);
    }

    @GetMapping("/public/cv")
    public ResponseEntity<Page<CV>> getAllCVs(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String languageCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
        @RequestParam(required = false, defaultValue = "false") Boolean isDeleted,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CV> cvPage = cvService.getAllCVsWithFilters(
            title,
            languageCode,
            status,
            createdAfter,
            createdBefore,
            isDeleted,
            pageable
        );
        return ResponseEntity.ok(cvPage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/public/cv/statistics")
    public ResponseEntity<CVStatisticsDTO> getCVStatistics() {
        CVStatisticsDTO statistics = cvService.getCVStatistics();
        return ResponseEntity.ok(statistics);
    }


    @PutMapping("/public/cv/{id}")
    public ResponseEntity<CV> updateCV(
        @PathVariable UUID id,
        @RequestBody CV updatedCV
    ) {
        CV updated = cvService.updateCV(id, updatedCV);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/public/cv/{id}")
    public ResponseEntity<CV> getCVById(@PathVariable UUID id) {
        return cvService.getCVById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/cv/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCV(@PathVariable UUID id) {
        cvService.deleteCV(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }
}



