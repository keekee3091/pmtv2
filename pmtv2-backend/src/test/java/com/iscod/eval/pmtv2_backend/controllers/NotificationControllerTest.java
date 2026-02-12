package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Notification;
import com.iscod.eval.pmtv2_backend.services.NotificationService;
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

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService service;

    @Test
    void shouldGetAllNotifications() throws Exception {
        Notification notif = Notification.builder().id(1L).message("Task assigned").build();
        when(service.getAll()).thenReturn(List.of(notif));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Task assigned"));
    }

    @Test
    void shouldGetNotificationByIdFound() throws Exception {
        Notification notif = Notification.builder().id(1L).message("Task assigned").build();
        when(service.getById(1L)).thenReturn(Optional.of(notif));

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task assigned"));
    }

    @Test
    void shouldGetNotificationByIdNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNotification() throws Exception {
        Notification notif = Notification.builder().id(1L).message("Task assigned").build();
        when(service.save(any(Notification.class))).thenReturn(notif);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Task assigned\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Task assigned"));
    }

    @Test
    void shouldUpdateNotificationFound() throws Exception {
        Notification notif = Notification.builder().id(1L).message("Updated").build();
        when(service.getById(1L)).thenReturn(Optional.of(notif));
        when(service.save(any(Notification.class))).thenReturn(notif);

        mockMvc.perform(put("/api/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Updated"));
    }

    @Test
    void shouldUpdateNotificationNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/notifications/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Updated\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteNotificationFound() throws Exception {
        Notification notif = Notification.builder().id(1L).message("Task assigned").build();
        when(service.getById(1L)).thenReturn(Optional.of(notif));
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/notifications/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteNotificationNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/notifications/99"))
                .andExpect(status().isNotFound());
    }
}
