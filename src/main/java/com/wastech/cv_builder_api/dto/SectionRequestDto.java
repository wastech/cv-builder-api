package com.wastech.cv_builder_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionRequestDto {
    @NotBlank(message = "Section type cannot be blank")
    private String type;

    @NotBlank(message = "Section title cannot be blank")
    private String title;

    @Min(value = 0, message = "Order index must be non-negative")
    private int orderIndex;

    private boolean visible = true;

    @NotNull(message = "CV ID cannot be null")
    private UUID cvId;  // ID of the CV this section belongs to
}