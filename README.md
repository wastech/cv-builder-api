
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


```// User Model
package com.portfolio.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String title;
    private String summary;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @ElementCollection
    private Set<String> skills;
}

// Experience Model
@Data
@Entity
@Table(name = "experiences")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String company;
    private String position;
    private String startDate;
    private String endDate;
    private String description;

    @ElementCollection
    private List<String> achievements;
}

// Project Model
@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;
    private String description;
    private String githubUrl;
    private String liveUrl;

    @ElementCollection
    private Set<String> technologies;

    @ElementCollection
    private List<String> highlights;
}

// Education Model
@Data
@Entity
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String institution;
    private String degree;
    private String field;
    private String startDate;
    private String endDate;
    private Double gpa;

    @ElementCollection
    private List<String> achievements;
}


Portfolio Theme Model

package com.portfolio.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "themes")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Column(columnDefinition = "TEXT")
    private String cssStyles;

    private boolean isDefault;
}```
