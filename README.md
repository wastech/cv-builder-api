
# CV/Resume Generation App

A feature-rich application that allows users to create, customize, and download professional CVs and resumes.

spell checker https://languagetool.org/

## Key Features

### 1. User Profile Management
- **Profile Creation**: Enable users to create and manage profiles with details like name, contact information, and job title.
- **Multiple CV/Resume Versions**: Allow users to save and manage multiple CV versions for different job applications.
- **Template Selection**: Offer a range of customizable templates (e.g., Classic, Modern, Minimalistic).

### 2. Content Sections Customization
- **Personal Information**: Include details like name, phone, email, LinkedIn profile, and website.
- **Professional Summary**: A concise summary of the user’s background and key skills.
- **Work Experience**: Add job titles, company names, locations, dates, and descriptions of responsibilities and achievements.
- **Education**: List institutions, degrees, fields of study, and graduation dates.
- **Skills Section**: Add both soft and hard skills relevant to the user’s profession.
- **Projects**: List projects with descriptions, technologies used, and links (e.g., GitHub, live URL).
- **Certifications and Awards**: Add recognitions, certifications, or awards.
- **Languages**: List languages and proficiency levels.
- **References**: Option to include references or indicate availability upon request.

### 3. Customizable Design Options
- **Font and Color Options**: Change fonts, colors, and sizes.
- **Section Reordering**: Rearrange sections with drag-and-drop functionality.
- **Margin and Spacing Adjustment**: Control padding, margins, and line spacing.
- **Visual Elements**: Add or remove borders, dividers, or icons.

### 4. Real-Time Preview
- **Live Preview**: See changes in real-time as users update information and styles.
- **Responsive Design**: Ensure the preview looks accurate across devices (desktop and mobile).

### 5. Download and Export Options
- **PDF Export**: Generate and download CVs/resumes as PDF files.
- **File Naming**: Customize file names based on template, job title, etc.
- **Printable Versions**: Ensure PDFs are print-ready.

### 6. Saving and Storage
- **Save Drafts**: Allow users to save drafts for later editing.
- **Cloud Storage**: Securely store user data and drafts.
- **Backup and Sync**: Sync data across devices or with services like Google Drive or Dropbox.

### 7. AI and Grammar Assistance
- **AI-Powered Content Suggestions**: Offer suggestions for summaries, skills, and job descriptions based on input or job title.
- **Grammar and Spell Check**: Integrated grammar and spell-checking.
- **Keyword Optimization**: Suggest keywords for improved ATS (Applicant Tracking System) compatibility.

### 8. Collaboration and Sharing
- **Link Sharing**: Generate a unique link for an online version of the CV.
- **Email Integration**: Send the CV directly to employers from within the app.
- **Feedback and Collaboration**: Share the CV with others for feedback before finalizing.

### 9. Analytics and Insights
- **Views and Engagement Metrics**: Track views of shared links.
- **ATS Compatibility Check**: Review and score the CV for ATS compatibility.

### 10. Subscription and Premium Features
- **Basic vs. Premium Plans**: Limit certain templates, downloads, or customizations for free users; offer premium features like unlimited downloads or additional templates.
- **Feature Upgrades**: Unlock advanced design options, AI recommendations, or additional storage with a premium plan.

### 11. Security and Privacy
- **Data Encryption**: Ensure all user data is securely encrypted.
- **GDPR Compliance**: Provide data export or deletion options to meet privacy regulations.
- **User Authentication**: Secure login options, potentially with two-factor authentication.

With these features, the app offers a comprehensive and intuitive experience, allowing users to create high-quality, professional CVs tailored to their needs.

package com.yourcompany.cvmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cv", 
    indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "language_code"),
        @Index(columnList = "created_at")
    }
)
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CvStatus status;

    @Column(name = "language_code", length = 10)
    private String languageCode;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<Section> sections;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "section", 
    indexes = {
        @Index(columnList = "cv_id,order_index"),
        @Index(columnList = "type")
    }
)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "is_visible")
    private boolean visible = true;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<SectionContent> contents;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "section_content", 
    indexes = {
        @Index(columnList = "section_id,order_index"),
        @Index(columnList = "content_type")
    }
)
public class SectionContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String content;

    @Column(name = "order_index")
    private int orderIndex;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_current")
    private boolean current;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "template", 
    indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "active")
    }
)
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String defaultStyles;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String layoutConfig;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

// Enum for CV Status
public enum CvStatus {
    DRAFT,
    IN_PROGRESS,
    COMPLETED,
    ARCHIVED
}

// PostgreSQL Specific Database Configuration
@Configuration
public class DatabaseConfig {
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/cvmanagement");
        dataSource.setUsername("your_username");
        dataSource.setPassword("your_password");
        return dataSource;
    }
}

// application.properties additional configurations
/*
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=update

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging SQL statements
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Connection pool configuration
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.idle-timeout=10000
*/```


# CV Management RESTful API Endpoints

## CV Endpoints
1. `GET /cvs`
   - List all CVs
   - Query parameters: 
     - `page` (pagination)
     - `size` (page size)
     - `sort` (sorting field)
     - `status` (filter by CV status)
     - `language` (filter by language)

2. `POST /cvs`
   - Create a new CV
   - Request body: CV object
   - Returns created CV with ID

3. `GET /cvs/{cvId}`
   - Retrieve a specific CV by ID
   - Includes full CV details with sections

4. `PUT /cvs/{cvId}`
   - Update an existing CV
   - Request body: Updated CV details

5. `DELETE /cvs/{cvId}`
   - Delete a specific CV

## Section Endpoints
6. `GET /cvs/{cvId}/sections`
   - List all sections for a specific CV
   - Query parameters:
     - `visible` (filter by visibility)
     - `type` (filter by section type)

7. `POST /cvs/{cvId}/sections`
   - Create a new section for a specific CV
   - Request body: Section object
   - Returns created section with ID

8. `GET /cvs/{cvId}/sections/{sectionId}`
   - Retrieve a specific section by ID

9. `PUT /cvs/{cvId}/sections/{sectionId}`
   - Update an existing section
   - Request body: Updated section details
   - Can modify title, visibility, order

10. `PATCH /cvs/{cvId}/sections/{sectionId}/order`
    - Update section order
    - Request body: New order index

11. `DELETE /cvs/{cvId}/sections/{sectionId}`
    - Delete a specific section

## Section Content Endpoints
12. `GET /cvs/{cvId}/sections/{sectionId}/contents`
    - List all content items in a section
    - Query parameters:
      - `current` (filter ongoing items)
      - `orderIndex` (sort order)

13. `POST /cvs/{cvId}/sections/{sectionId}/contents`
    - Create a new content item in a section
    - Request body: SectionContent object
    - Returns created content with ID

14. `GET /cvs/{cvId}/sections/{sectionId}/contents/{contentId}`
    - Retrieve a specific content item

15. `PUT /cvs/{cvId}/sections/{sectionId}/contents/{contentId}`
    - Update an existing content item
    - Can modify content, dates, current status

16. `PATCH /cvs/{cvId}/sections/{sectionId}/contents/{contentId}/order`
    - Update content item order within section

17. `DELETE /cvs/{cvId}/sections/{sectionId}/contents/{contentId}`
    - Delete a specific content item

## Template Endpoints
18. `GET /templates`
    - List all available templates
    - Query parameters:
      - `active` (filter active templates)
      - `page`, `size`, `sort`

19. `GET /templates/{templateId}`
    - Retrieve a specific template details
    - Includes layout and styling information

20. `POST /cvs/{cvId}/apply-template/{templateId}`
    - Apply a specific template to an existing CV
    - Returns updated CV with new template configuration

## Bulk Operations
21. `POST /cvs/bulk-create`
    - Create multiple CVs in one request
    - Request body: Array of CV objects

22. `POST /cvs/{cvId}/sections/bulk-create`
    - Create multiple sections for a CV in one request
    - Request body: Array of Section objects

## Search and Advanced Queries
23. `GET /cvs/search`
    - Advanced search across CVs
    - Query parameters:
      - `title` (partial match)
      - `languageCode`
      - `createdAfter`
      - `createdBefore`

## Metadata and Stats
24. `GET /cvs/stats`
    - Retrieve aggregate statistics
    - Number of CVs
    - CVs by language
    - CVs by status
