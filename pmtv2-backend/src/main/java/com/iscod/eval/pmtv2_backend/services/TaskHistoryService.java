package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.repositories.TaskHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskHistoryService {

    private final TaskHistoryRepository repository;

    public TaskHistoryService(TaskHistoryRepository repository) {
        this.repository = repository;
    }

    public List<TaskHistory> getAll() {
        return repository.findAll();
    }

    public List<TaskHistory> getByTaskId(Long taskId) {
        return repository.findByTaskId(taskId);
    }

    public TaskHistory save(TaskHistory history) {
        return repository.save(history);
    }
}
