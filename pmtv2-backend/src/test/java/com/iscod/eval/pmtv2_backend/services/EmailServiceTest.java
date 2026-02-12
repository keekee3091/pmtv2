package com.iscod.eval.pmtv2_backend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService service;

    @Test
    void shouldSendTaskAssignedNotification() {
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));

        service.sendTaskAssignedNotification("bob@example.com", "Backend setup", "Alice");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertThat(sent.getTo()).contains("bob@example.com");
        assertThat(sent.getSubject()).isEqualTo("Nouvelle tâche assignée");
        assertThat(sent.getText()).contains("Backend setup");
        assertThat(sent.getText()).contains("Alice");
    }

    @Test
    void shouldThrowWhenMailFails() {
        doThrow(new RuntimeException("SMTP error")).when(emailSender).send(any(SimpleMailMessage.class));

        assertThatThrownBy(() ->
                service.sendTaskAssignedNotification("bob@example.com", "Task", "Alice"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erreur lors de l'envoi de l'email");
    }
}
