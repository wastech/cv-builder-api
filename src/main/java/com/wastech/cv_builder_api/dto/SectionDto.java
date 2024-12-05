package com.wastech.cv_builder_api.dto;

import jakarta.validation.constraints.Min;
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
public class SectionDto {
    private UUID id;

    @NotBlank(message = "Section type cannot be blank")
    private String type;

    @NotBlank(message = "Section title cannot be blank")
    private String title;

    @Min(value = 0, message = "Order index must be non-negative")
    private int orderIndex;

    private boolean visible = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UUID cvId;

    private List<SectionContentDto> contents;
}