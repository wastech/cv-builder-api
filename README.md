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
}```
