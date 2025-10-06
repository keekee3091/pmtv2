package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.services.TaskHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class TaskHistoryController {

    private final TaskHistoryService service;

    public TaskHistoryController(TaskHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskHistory> getAll() {
        return service.getAll();
    }

    @GetMapping("/task/{taskId}")
    public List<TaskHistory> getByTask(@PathVariable Long taskId) {
        return service.getByTaskId(taskId);
    }

    @PostMapping
    public ResponseEntity<TaskHistory> create(@RequestBody TaskHistory history) {
        return ResponseEntity.ok(service.save(history));
    }
}
