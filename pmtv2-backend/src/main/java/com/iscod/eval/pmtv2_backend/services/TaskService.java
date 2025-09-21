package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) { this.repo = repo; }

    public List<Task> getAll() { return repo.findAll(); }
    public Optional<Task> getById(Long id) { return repo.findById(id); }
    public Task save(Task t) { return repo.save(t); }
    public void delete(Long id) { repo.deleteById(id); }
}
