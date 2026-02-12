package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Notification;
import com.iscod.eval.pmtv2_backend.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository repo;

    @InjectMocks
    private NotificationService service;

    @Test
    void shouldReturnAllNotifications() {
        Notification n = Notification.builder().id(1L).message("Task assigned").build();
        when(repo.findAll()).thenReturn(List.of(n));

        List<Notification> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Task assigned");
    }

    @Test
    void shouldReturnNotificationById() {
        Notification n = Notification.builder().id(1L).message("Task assigned").build();
        when(repo.findById(1L)).thenReturn(Optional.of(n));

        Optional<Notification> result = service.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getMessage()).isEqualTo("Task assigned");
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Notification> result = service.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveNotification() {
        Notification n = Notification.builder().id(1L).message("New").build();
        when(repo.save(n)).thenReturn(n);

        Notification result = service.save(n);

        assertThat(result.getMessage()).isEqualTo("New");
    }

    @Test
    void shouldDeleteNotification() {
        service.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
