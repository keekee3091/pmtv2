package com.iscod.eval.pmtv2_backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- Relations ---
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ProjectMember> projectMemberships = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private Set<Project> ownedProjects = new HashSet<>();

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private Set<Task> assignedTasks = new HashSet<>();

    @OneToMany(mappedBy = "changedBy")
    @JsonIgnore
    private Set<TaskHistory> taskHistories = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Notification> notifications = new HashSet<>();
}
