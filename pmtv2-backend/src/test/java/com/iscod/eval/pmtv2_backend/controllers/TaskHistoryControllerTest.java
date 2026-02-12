package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.services.TaskHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskHistoryController.class)
class TaskHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskHistoryService service;

    @Test
    void shouldGetAllHistory() throws Exception {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("TODO").newValue("DONE").build();
        when(service.getAll()).thenReturn(List.of(h));

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].oldValue").value("TODO"));
    }

    @Test
    void shouldGetHistoryByTaskId() throws Exception {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("TODO").newValue("IN_PROGRESS").build();
        when(service.getByTaskId(1L)).thenReturn(List.of(h));

        mockMvc.perform(get("/api/history/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].newValue").value("IN_PROGRESS"));
    }

    @Test
    void shouldGetEmptyHistoryForUnknownTask() throws Exception {
        when(service.getByTaskId(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/history/task/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void shouldCreateHistory() throws Exception {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("TODO").newValue("DONE")
                .changeDate(LocalDateTime.of(2025, 1, 15, 10, 0)).build();
        when(service.save(any(TaskHistory.class))).thenReturn(h);

        mockMvc.perform(post("/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldValue\": \"TODO\", \"newValue\": \"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newValue").value("DONE"));
    }
}
