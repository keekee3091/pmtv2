package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.ProjectRepository;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import com.iscod.eval.pmtv2_backend.services.ProjectService;
import com.iscod.eval.pmtv2_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ProjectService service;
    @MockitoBean private UserService userService;
    @MockitoBean private ProjectRepository projectRepository;
    @MockitoBean private TaskRepository taskRepository;

    @Test
    void shouldGetAllProjects() throws Exception {
        Project project = Project.builder().id(1L).name("P1").build();
        when(service.getAll()).thenReturn(List.of(project));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("P1"));
    }

    @Test
    void shouldGetProjectByIdFound() throws Exception {
        Project project = Project.builder().id(1L).name("P1").build();
        when(service.getById(1L)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("P1"));
    }

    @Test
    void shouldGetProjectByIdNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/projects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateProject() throws Exception {
        User owner = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        Project project = Project.builder().id(1L).name("P1").owner(owner).build();
        when(userService.getById(1L)).thenReturn(Optional.of(owner));
        when(service.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"P1\", \"description\": \"desc\", \"startDate\": \"2025-01-01\", \"ownerId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("P1"));
    }

    @Test
    void shouldUpdateProjectFound() throws Exception {
        Project project = Project.builder().id(1L).name("Updated P1").build();
        when(service.getById(1L)).thenReturn(Optional.of(project));
        when(service.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated P1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated P1"));
    }

    @Test
    void shouldUpdateProjectNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/projects/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Whatever\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteProjectFound() throws Exception {
        Project project = Project.builder().id(1L).name("P1").build();
        when(service.getById(1L)).thenReturn(Optional.of(project));
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteProjectNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/projects/99"))
                .andExpect(status().isNotFound());
    }
}
