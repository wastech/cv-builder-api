package com.wastech.cv_builder_api.dto;

import com.wastech.cv_builder_api.model.CvStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CvDto {
    private UUID id;

    @NotBlank(message = "CV title cannot be blank")
    private String title;

    private String summary;

    private CvStatus status;

    private String languageCode;

    private Boolean isDeleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<SectionDto> sections;

    private TemplateDto template;
}