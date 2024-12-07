package com.wastech.cv_builder_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {
    private UUID id;

    @NotBlank(message = "Template name cannot be blank")
    private String name;

    private String description;

    private String defaultStyles;

    private String layoutConfig;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

//    private List<UUID> cvIds;

}