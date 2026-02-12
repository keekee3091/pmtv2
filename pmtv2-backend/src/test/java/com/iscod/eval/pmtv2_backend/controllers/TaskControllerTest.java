package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import com.iscod.eval.pmtv2_backend.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private TaskService service;
    @MockitoBean private TaskHistoryService historyService;
    @MockitoBean private UserService userService;
    @MockitoBean private TaskRepository taskRepository;
    @MockitoBean private ProjectService projectService;
    @MockitoBean private EmailService emailService;

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
    void shouldCreateTaskForProject() throws Exception {
        Project project = Project.builder().id(1L).name("P1").build();
        User assignee = User.builder().id(2L).username("Bob").email("bob@example.com").build();
        Task savedTask = Task.builder().id(1L).name("New Task").status("TODO").project(project).assignedTo(assignee).build();

        when(projectService.getById(1L)).thenReturn(Optional.of(project));
        when(userService.getById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        doNothing().when(emailService).sendTaskAssignedNotification(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/tasks/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Task\", \"description\": \"desc\", \"status\": \"TODO\", \"priority\": \"HIGH\", \"assignedTo\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void shouldCreateTaskForProjectNotFound() throws Exception {
        when(projectService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/tasks/99/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Task\", \"description\": \"desc\", \"status\": \"TODO\", \"priority\": \"HIGH\", \"assignedTo\": 1}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateTaskForProjectWithoutAssigneeEmail() throws Exception {
        Project project = Project.builder().id(1L).name("P1").build();
        User assignee = User.builder().id(2L).username("Bob").email(null).build();
        Task savedTask = Task.builder().id(1L).name("Task").status("TODO").project(project).assignedTo(assignee).build();

        when(projectService.getById(1L)).thenReturn(Optional.of(project));
        when(userService.getById(2L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks/1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Task\", \"description\": \"desc\", \"status\": \"TODO\", \"priority\": \"HIGH\", \"assignedTo\": 2}"))
                .andExpect(status().isOk());

        verify(emailService, never()).sendTaskAssignedNotification(anyString(), anyString(), anyString());
    }

    @Test
    void shouldUpdateTaskWithAllFieldChanges() throws Exception {
        User oldAssignee = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        User newAssignee = User.builder().id(2L).username("Bob").email("bob@example.com").build();
        Task existing = Task.builder().id(1L).name("Old").description("Old desc").status("TODO")
                .priority("LOW").assignedTo(oldAssignee).build();
        Task updated = Task.builder().id(1L).name("New").description("New desc").status("DONE")
                .priority("HIGH").assignedTo(newAssignee).build();

        when(service.getById(1L)).thenReturn(Optional.of(existing));
        when(userService.getById(2L)).thenReturn(Optional.of(newAssignee));
        when(historyService.save(any(TaskHistory.class))).thenReturn(TaskHistory.builder().build());
        when(service.save(any(Task.class))).thenReturn(updated);
        doNothing().when(emailService).sendTaskAssignedNotification(anyString(), anyString(), anyString());

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New\", \"description\": \"New desc\", \"status\": \"DONE\", \"priority\": \"HIGH\", \"assignedTo\": 2, \"changedBy\": 1}"))
                .andExpect(status().isOk());

        verify(historyService, atLeast(4)).save(any(TaskHistory.class));
    }

    @Test
    void shouldUpdateTaskWithNullAssignee() throws Exception {
        User oldAssignee = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        Task existing = Task.builder().id(1L).name("Task").status("TODO").assignedTo(oldAssignee).build();
        Task updated = Task.builder().id(1L).name("Task").status("TODO").assignedTo(null).build();

        when(service.getById(1L)).thenReturn(Optional.of(existing));
        when(historyService.save(any(TaskHistory.class))).thenReturn(TaskHistory.builder().build());
        when(service.save(any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Task\", \"status\": \"TODO\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTaskWithDueDate() throws Exception {
        Task existing = Task.builder().id(1L).name("Task").status("TODO").build();
        Task updated = Task.builder().id(1L).name("Task").status("TODO").build();

        when(service.getById(1L)).thenReturn(Optional.of(existing));
        when(historyService.save(any(TaskHistory.class))).thenReturn(TaskHistory.builder().build());
        when(service.save(any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Task\", \"status\": \"TODO\", \"dueDate\": \"2025-06-01\", \"changedBy\": 1}"))
                .andExpect(status().isOk());
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
    void shouldAssignTask() throws Exception {
        User assignee = User.builder().id(2L).username("Bob").email("bob@example.com").build();
        Task task = Task.builder().id(1L).name("Task").status("TODO").build();
        Task assigned = Task.builder().id(1L).name("Task").status("TODO").assignedTo(assignee).build();

        when(service.getById(1L)).thenReturn(Optional.of(task));
        when(userService.getById(2L)).thenReturn(Optional.of(assignee));
        when(historyService.save(any(TaskHistory.class))).thenReturn(TaskHistory.builder().build());
        when(service.save(any(Task.class))).thenReturn(assigned);

        mockMvc.perform(post("/api/tasks/1/assign?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedTo.username").value("Bob"));
    }

    @Test
    void shouldAssignTaskNotFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());
        when(userService.getById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/tasks/99/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 2}"))
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

    @Test
    void shouldUpdateTaskWithNoChanges() throws Exception {
        Task existing = Task.builder().id(1L).name("Same").description("Same desc").status("TODO")
                .priority("MEDIUM").build();

        when(service.getById(1L)).thenReturn(Optional.of(existing));
        when(service.save(any(Task.class))).thenReturn(existing);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Same\", \"description\": \"Same desc\", \"status\": \"TODO\", \"priority\": \"MEDIUM\"}"))
                .andExpect(status().isOk());

        verify(historyService, never()).save(any(TaskHistory.class));
    }

    @Test
    void shouldUpdateTaskWithNullChangedBy() throws Exception {
        Task existing = Task.builder().id(1L).name("Old").status("TODO").build();
        Task updated = Task.builder().id(1L).name("New").status("TODO").build();

        when(service.getById(1L)).thenReturn(Optional.of(existing));
        when(historyService.save(any(TaskHistory.class))).thenReturn(TaskHistory.builder().build());
        when(service.save(any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New\", \"status\": \"TODO\"}"))
                .andExpect(status().isOk());
    }
}
