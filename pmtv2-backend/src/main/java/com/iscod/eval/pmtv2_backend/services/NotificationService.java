package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Notification;
import com.iscod.eval.pmtv2_backend.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) { this.repo = repo; }

    public List<Notification> getAll() { return repo.findAll(); }
    public Optional<Notification> getById(Long id) { return repo.findById(id); }
    public Notification save(Notification n) { return repo.save(n); }
    public void delete(Long id) { repo.deleteById(id); }
}
