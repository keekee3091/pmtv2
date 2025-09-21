package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import com.iscod.eval.pmtv2_backend.services.TaskService;
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

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService service;

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task = Task.builder().id(1L).name("Backend").status("TODO").build();
        when(service.getAll()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Backend"));
    }

    @Test
    void shouldGetTaskByIdFound() throws Exception {
        Task task = Task.builder().id(1L).name("Backend").status("TODO").build();
        when(service.getById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Backend"));
    }

    @Test
    void shouldGetTaskByIdNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = Task.builder().id(1L).name("Backend").status("TODO").build();
        when(service.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Backend\", \"status\": \"TODO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Backend"));
    }

    @Test
    void shouldUpdateTaskFound() throws Exception {
        Task task = Task.builder().id(1L).name("Backend Updated").status("DONE").build();
        when(service.getById(1L)).thenReturn(Optional.of(task));
        when(service.save(any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Backend Updated\", \"status\": \"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Backend Updated"));
    }

    @Test
    void shouldUpdateTaskNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Something\", \"status\": \"DONE\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTaskFound() throws Exception {
        Task task = Task.builder().id(1L).name("Backend").build();
        when(service.getById(1L)).thenReturn(Optional.of(task));
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteTaskNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}
