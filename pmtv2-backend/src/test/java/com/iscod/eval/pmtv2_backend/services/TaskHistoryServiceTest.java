package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.repositories.TaskHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskHistoryServiceTest {

    @Mock
    private TaskHistoryRepository repository;

    @InjectMocks
    private TaskHistoryService service;

    @Test
    void shouldReturnAllHistory() {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("TODO").newValue("DONE").build();
        when(repository.findAll()).thenReturn(List.of(h));

        List<TaskHistory> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOldValue()).isEqualTo("TODO");
    }

    @Test
    void shouldReturnHistoryByTaskId() {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("TODO").newValue("IN_PROGRESS").build();
        when(repository.findByTaskId(1L)).thenReturn(List.of(h));

        List<TaskHistory> result = service.getByTaskId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNewValue()).isEqualTo("IN_PROGRESS");
    }

    @Test
    void shouldReturnEmptyHistoryForUnknownTask() {
        when(repository.findByTaskId(99L)).thenReturn(List.of());

        List<TaskHistory> result = service.getByTaskId(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveHistory() {
        TaskHistory h = TaskHistory.builder().id(1L).oldValue("OLD").newValue("NEW")
                .changeDate(LocalDateTime.now()).build();
        when(repository.save(h)).thenReturn(h);

        TaskHistory result = service.save(h);

        assertThat(result.getNewValue()).isEqualTo("NEW");
    }
}
