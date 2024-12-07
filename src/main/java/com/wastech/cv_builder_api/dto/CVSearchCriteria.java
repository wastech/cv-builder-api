package com.wastech.cv_builder_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CVSearchCriteria {
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
    private String languageCode;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    private Boolean isDeleted;

    private LocalDateTime createdAfter;

    private LocalDateTime createdBefore;
}
