package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TaskHistoryModelTest {

    @Test
    void testBuilderAndGetters() {
        User user = User.builder().id(1L).username("Alice").build();
        Task task = Task.builder().id(1L).name("Backend").build();
        LocalDateTime now = LocalDateTime.now();

        TaskHistory history = TaskHistory.builder()
                .id(1L)
                .oldValue("TODO")
                .newValue("DONE")
                .changeDate(now)
                .task(task)
                .changedBy(user)
                .build();

        assertThat(history.getId()).isEqualTo(1L);
        assertThat(history.getOldValue()).isEqualTo("TODO");
        assertThat(history.getNewValue()).isEqualTo("DONE");
        assertThat(history.getChangeDate()).isEqualTo(now);
        assertThat(history.getTask()).isEqualTo(task);
        assertThat(history.getChangedBy()).isEqualTo(user);
    }

    @Test
    void testSetters() {
        TaskHistory history = new TaskHistory();
        history.setId(2L);
        history.setOldValue("IN_PROGRESS");
        history.setNewValue("DONE");

        assertThat(history.getId()).isEqualTo(2L);
        assertThat(history.getOldValue()).isEqualTo("IN_PROGRESS");
        assertThat(history.getNewValue()).isEqualTo("DONE");
    }
}
