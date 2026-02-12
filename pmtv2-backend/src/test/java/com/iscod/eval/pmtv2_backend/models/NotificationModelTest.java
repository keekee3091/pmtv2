package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationModelTest {

    @Test
    void testBuilderAndGetters() {
        User user = User.builder().id(1L).username("Alice").build();
        Task task = Task.builder().id(1L).name("Backend").build();

        Notification notif = Notification.builder()
                .id(1L)
                .message("Task assigned")
                .isRead(false)
                .user(user)
                .task(task)
                .build();

        assertThat(notif.getId()).isEqualTo(1L);
        assertThat(notif.getMessage()).isEqualTo("Task assigned");
        assertThat(notif.getIsRead()).isFalse();
        assertThat(notif.getUser()).isEqualTo(user);
        assertThat(notif.getTask()).isEqualTo(task);
    }

    @Test
    void testSetters() {
        Notification notif = new Notification();
        notif.setId(2L);
        notif.setMessage("Updated message");
        notif.setIsRead(true);

        assertThat(notif.getId()).isEqualTo(2L);
        assertThat(notif.getMessage()).isEqualTo("Updated message");
        assertThat(notif.getIsRead()).isTrue();
    }
}
