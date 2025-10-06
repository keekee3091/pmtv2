package com.iscod.eval.pmtv2_backend.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendTaskAssignedNotification(String toEmail, String taskName, String assigneeName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Nouvelle tâche assignée");
        message.setText("Bonjour,\n\n" +
                "Une nouvelle tâche intitulée '" + taskName + "' vous a été assignée par " + assigneeName + ".\n\n" +
                "Cordialement,\nL'équipe");

        try {
            emailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }
}
