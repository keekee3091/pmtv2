package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.models.ProjectMember;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.services.ProjectMemberService;
import com.iscod.eval.pmtv2_backend.services.ProjectService;
import com.iscod.eval.pmtv2_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectMemberController.class)
class ProjectMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private ProjectMemberService service;
    @MockitoBean private UserService userService;
    @MockitoBean private ProjectService projectService;

    @Test
    void shouldGetAllMembers() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        ProjectMember pm = ProjectMember.builder().id(1L).user(user).role("ADMIN").build();
        when(service.getAll()).thenReturn(List.of(pm));

        mockMvc.perform(get("/api/project-members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].role").value("ADMIN"));
    }

    @Test
    void shouldGetMemberByIdFound() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        ProjectMember pm = ProjectMember.builder().id(1L).user(user).role("MEMBER").build();
        when(service.getById(1L)).thenReturn(Optional.of(pm));

        mockMvc.perform(get("/api/project-members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("MEMBER"));
    }

    @Test
    void shouldGetMemberByIdNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/project-members/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetUsersByProject() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(service.getUsersByProjectId(1L)).thenReturn(List.of(user));

        mockMvc.perform(get("/api/project-members/1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Alice"));
    }

    @Test
    void shouldCreateMember() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        Project project = Project.builder().id(1L).name("P1").build();
        ProjectMember pm = ProjectMember.builder().id(1L).user(user).project(project).role("ADMIN").build();

        when(userService.getById(1L)).thenReturn(Optional.of(user));
        when(projectService.getById(1L)).thenReturn(Optional.of(project));
        when(service.save(any(ProjectMember.class))).thenReturn(pm);

        mockMvc.perform(post("/api/project-members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\": 1, \"project\": 1, \"role\": \"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void shouldUpdateMemberFound() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        ProjectMember pm = ProjectMember.builder().id(1L).user(user).role("OBSERVER").build();
        when(service.getById(1L)).thenReturn(Optional.of(pm));
        when(service.save(any(ProjectMember.class))).thenReturn(pm);

        mockMvc.perform(put("/api/project-members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\": \"OBSERVER\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateMemberNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/project-members/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\": \"MEMBER\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMemberFound() throws Exception {
        ProjectMember pm = ProjectMember.builder().id(1L).role("MEMBER").build();
        when(service.getById(1L)).thenReturn(Optional.of(pm));
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/project-members/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteMemberNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/project-members/99"))
                .andExpect(status().isNotFound());
    }
}
