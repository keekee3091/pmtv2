package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @Test
    void shouldRegisterNewUser() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").password("pwd").build();
        when(service.existsByEmail("alice@example.com")).thenReturn(false);
        when(service.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Alice\", \"email\": \"alice@example.com\", \"password\": \"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alice"));
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        when(service.existsByEmail("alice@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"Alice\", \"email\": \"alice@example.com\", \"password\": \"pwd\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").password("pwd").build();
        when(service.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"alice@example.com\", \"password\": \"pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Alice"));
    }

    @Test
    void shouldRejectInvalidPassword() throws Exception {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").password("pwd").build();
        when(service.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"alice@example.com\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectUnknownEmail() throws Exception {
        when(service.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"unknown@example.com\", \"password\": \"pwd\"}"))
                .andExpect(status().isUnauthorized());
    }
}
