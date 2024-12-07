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
public class CVStatisticsDTO {
    @Builder.Default
    private long totalActiveCVs = 0;

    @Builder.Default
    private Map<String, Long> cvsByLanguage = new HashMap<>();

    @Builder.Default
    private Map<String, Long> cvsByStatus = new HashMap<>();

    // Additional helper methods
    public void addLanguageCount(String language, Long count) {
        cvsByLanguage.put(language, count);
    }

    public void addStatusCount(String status, Long count) {
        cvsByStatus.put(status, count);
    }

    public Long getLanguageCount(String language) {
        return cvsByLanguage.getOrDefault(language, 0L);
    }

    public Long getStatusCount(String status) {
        return cvsByStatus.getOrDefault(status, 0L);
    }
}