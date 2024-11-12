
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


```package com.portfolio.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    @Column(length = 100)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CV> cvs = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SocialLink> socialLinks = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private boolean active = true;

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}

@Data
@Entity
@Table(name = "cvs")
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "CV title is required")
    private String title;

    private String slug;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @Column(name = "template_name")
    private String templateName;

    private boolean isPublic = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}

@Data
@Entity
@Table(name = "experiences")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(length = 2000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "experience_achievements", 
                    joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "achievement", length = 500)
    private List<String> achievements = new ArrayList<>();

    private boolean current = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

@Data
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @NotBlank(message = "Skill name is required")
    private String name;

    @Enumerated(EnumType.STRING)
    private SkillLevel level;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    private Integer yearsOfExperience;

    public enum SkillLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    public enum SkillCategory {
        TECHNICAL, SOFT_SKILL, LANGUAGE, TOOL, OTHER
    }
}

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @NotBlank(message = "Project name is required")
    private String name;

    @Column(length = 2000)
    private String description;

    @Pattern(regexp = "^(https?://)?github\\.com/.+$", message = "Please provide a valid GitHub URL")
    private String githubUrl;

    @Pattern(regexp = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", 
            message = "Please provide a valid URL")
    private String liveUrl;

    @ElementCollection
    @CollectionTable(name = "project_technologies", 
                    joinColumns = @JoinColumn(name = "project_id"))
    private Set<String> technologies = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "project_highlights", 
                    joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "highlight", length = 500)
    private List<String> highlights = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean featured;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

@Data
@Entity
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id")
    private CV cv;

    @NotBlank(message = "Institution name is required")
    private String institution;

    @NotBlank(message = "Degree is required")
    private String degree;

    private String field;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @DecimalMin(value = "0.0", message = "GPA must be greater than or equal to 0")
    @DecimalMax(value = "4.0", message = "GPA must be less than or equal to 4.0")
    private Double gpa;

    @ElementCollection
    @CollectionTable(name = "education_achievements", 
                    joinColumns = @JoinColumn(name = "education_id"))
    @Column(name = "achievement", length = 500)
    private List<String> achievements = new ArrayList<>();

    private boolean current = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

@Data
@Entity
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Theme name is required")
    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String cssStyles;

    @Column(name = "preview_image_url")
    private String previewImageUrl;

    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

@Data
@Entity
@Table(name = "social_links")
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "Platform name is required")
    private String platform;

    @NotBlank(message = "URL is required")
    @Pattern(regexp = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
            message = "Please provide a valid URL")
    private String url;

    private String icon;
}```
