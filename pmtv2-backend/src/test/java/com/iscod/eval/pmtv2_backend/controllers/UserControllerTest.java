package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.User;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void shouldGetAllUsers() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(service.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("Alice"));
    }

    @Test
    void shouldGetUserByIdFound() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(service.getById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alice"));
    }

    @Test
    void shouldGetUserByIdNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(service.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Alice\", \"email\": \"alice@example.com\", \"password\": \"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alice"));
    }

    @Test
    void shouldUpdateUserFound() throws Exception {
        User user = User.builder().id(1L).username("Alice Updated").email("alice@example.com").build();
        when(service.getById(1L)).thenReturn(Optional.of(user));
        when(service.save(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Alice Updated\", \"email\": \"alice@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alice Updated"));
    }

    @Test
    void shouldUpdateUserNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Bob\", \"email\": \"bob@example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePasswordFound() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").password("old").build();
        when(service.getById(1L)).thenReturn(Optional.of(user));
        when(service.save(any(User.class))).thenReturn(user);

        mockMvc.perform(patch("/api/users/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"newpwd\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdatePasswordNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/users/99/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"newpwd\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteUserFound() throws Exception {
        User user = User.builder().id(1L).username("Alice").build();
        when(service.getById(1L)).thenReturn(Optional.of(user));
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteUserNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }
}
