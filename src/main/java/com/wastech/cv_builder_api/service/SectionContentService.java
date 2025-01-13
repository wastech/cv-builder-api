// SectionContentService.java
package com.wastech.cv_builder_api.service;

import com.wastech.cv_builder_api.dto.SectionContentDto;
import java.util.List;
import java.util.UUID;

public interface SectionContentService {
    /**
     * Create a new section content
     * @param sectionContentDto the section content data
     * @return the created section content
     */
    SectionContentDto createSectionContent(SectionContentDto sectionContentDto);

    /**
     * Update an existing section content
     * @param id the id of the section content to update
     * @param sectionContentDto the updated section content data
     * @return the updated section content
     */
    SectionContentDto updateSectionContent(UUID id, SectionContentDto sectionContentDto);

    /**
     * Get a section content by its id
     * @param id the id of the section content
     * @return the section content
     */
    SectionContentDto getSectionContentById(UUID id);

    /**
     * Get all section contents
     * @return list of all section contents
     */
    List<SectionContentDto> getAllSectionContents();

    /**
     * Get all section contents for a specific section
     * @param sectionId the id of the section
     * @return list of section contents
     */
    List<SectionContentDto> getSectionContentsBySection(UUID sectionId);

    /**
     * Delete a section content
     * @param id the id of the section content to delete
     */
    void deleteSectionContent(UUID id);

    /**
     * Delete all section contents for a specific section
     * @param sectionId the id of the section
     */
    void deleteAllSectionContentsBySection(UUID sectionId);
}