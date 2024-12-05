package com.wastech.cv_builder_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionContentDto {
    private UUID id;

    @NotNull(message = "Content type cannot be null")
    private String contentType;

    private String content; // JSONB content

    @Min(value = 0, message = "Order index must be non-negative")
    private int orderIndex;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean current;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UUID sectionId;
}