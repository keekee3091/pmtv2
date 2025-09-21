package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TaskModelTest {

    @Test
    void testBuilderAndGetters() {
        Project project = Project.builder().id(1L).name("Proj A").build();
        User user = User.builder().id(1L).username("Alice").build();

        Task task = Task.builder()
                .id(1L)
                .name("Backend setup")
                .description("Spring Boot init")
                .dueDate(LocalDate.of(2025, 1, 15))
                .priority("HIGH")
                .status("TODO")
                .project(project)
                .assignedTo(user)
                .build();

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getName()).isEqualTo("Backend setup");
        assertThat(task.getPriority()).isEqualTo("HIGH");
        assertThat(task.getStatus()).isEqualTo("TODO");
        assertThat(task.getProject()).isEqualTo(project);
        assertThat(task.getAssignedTo()).isEqualTo(user);
    }

    @Test
    void testSettersAndCollections() {
        Task task = new Task();
        task.setId(2L);
        task.setName("Frontend");
        task.setStatus("IN_PROGRESS");

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getName()).isEqualTo("Frontend");
        assertThat(task.getStatus()).isEqualTo("IN_PROGRESS");

        // relations
        assertThat(task.getHistory()).isNotNull();
        assertThat(task.getNotifications()).isNotNull();
    }
}
