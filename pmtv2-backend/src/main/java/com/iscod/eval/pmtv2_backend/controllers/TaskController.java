package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import com.iscod.eval.pmtv2_backend.services.ProjectService;
import com.iscod.eval.pmtv2_backend.services.TaskService;
import com.iscod.eval.pmtv2_backend.services.TaskHistoryService;
import com.iscod.eval.pmtv2_backend.services.UserService;
import com.iscod.eval.pmtv2_backend.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;
    private final TaskHistoryService historyService;
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final EmailService emailService;

    public TaskController(TaskService service, TaskHistoryService historyService, UserService userService, TaskRepository taskRepository, ProjectService projectService, EmailService emailService) {
        this.service = service;
        this.historyService = historyService;
        this.userService = userService;
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.emailService = emailService;
    }

    @GetMapping
    public List<Task> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@RequestBody Task task) {
        return ResponseEntity.ok(service.save(task));
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long assigneeId = Long.valueOf(body.get("assignedTo").toString());

        return projectService.getById(id)
                .map(project -> {
                    Task task = new Task();
                    task.setName((String) body.get("name"));
                    task.setDescription((String) body.get("description"));
                    task.setStatus((String) body.get("status"));
                    task.setPriority((String) body.get("priority"));
                    task.setProject(project);

                    if (assigneeId != null) {
                        userService.getById(assigneeId).ifPresent(task::setAssignedTo);
                    }

                    Task savedTask = taskRepository.save(task);

                    if (savedTask.getAssignedTo() != null && savedTask.getAssignedTo().getEmail() != null) {
                        emailService.sendTaskAssignedNotification(
                                savedTask.getAssignedTo().getEmail(),
                                savedTask.getName(),
                                savedTask.getAssignedTo().getUsername()
                        );
                    }

                    return ResponseEntity.ok(savedTask);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id,  @RequestBody Map<String, Object> body, @RequestParam(required = false) Long userId) {

        Long assigneeId = body.get("assignedTo") != null ? Long.valueOf(body.get("assignedTo").toString()) : null;

        return service.getById(id).map(existing -> {

            String newName = (String) body.get("name");
            String newDescription = (String) body.get("description");
            String newStatus = (String) body.get("status");
            String newPriority = (String) body.get("priority");
            LocalDate newDueDate = body.get("dueDate") != null ? LocalDate.parse(body.get("dueDate").toString()) : null;

            if (hasChanged(existing.getName(), newName)) {
                createHistory(existing, "name", existing.getName(), newName, userId);
                existing.setName(newName);
            }

            if (hasChanged(existing.getDescription(), newDescription)) {
                createHistory(existing, "description", existing.getDescription(), newDescription, userId);
                existing.setDescription(newDescription);
            }

            if (hasChanged(existing.getStatus(), newStatus)) {
                createHistory(existing, "status", existing.getStatus(), newStatus, userId);
                existing.setStatus(newStatus);
            }

            if (hasChanged(existing.getPriority(), newPriority)) {
                createHistory(existing, "priority", existing.getPriority(), newPriority, userId);
                existing.setPriority(newPriority);
            }

            if (hasChanged(existing.getDueDate(), newDueDate)) {
                createHistory(existing, "dueDate",
                        String.valueOf(existing.getDueDate()),
                        String.valueOf(newDueDate), userId);
                existing.setDueDate(newDueDate);
            }

            Long oldUserId = existing.getAssignedTo() != null ? existing.getAssignedTo().getId() : null;
            if (hasChanged(oldUserId, assigneeId)) {
                createHistory(existing, "assignedTo", String.valueOf(oldUserId), String.valueOf(assigneeId), userId);

                if (assigneeId != null) {
                    userService.getById(assigneeId).ifPresent(existing::setAssignedTo);
                } else {
                    existing.setAssignedTo(null);
                }

                if (existing.getAssignedTo() != null && existing.getAssignedTo().getEmail() != null) {
                    emailService.sendTaskAssignedNotification(
                            existing.getAssignedTo().getEmail(),
                            existing.getName(),
                            existing.getAssignedTo().getUsername()
                    );
                }
            }

            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(service.save(existing));

        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Task> assign(@PathVariable Long id, @RequestBody Map<String, Long> body, @RequestParam(required = false) Long userId) {
        Long assigneeId = body.get("userId");

        Optional<Task> taskOpt = service.getById(id);
        Optional<User> assigneeOpt = userService.getById(assigneeId);

        if (taskOpt.isPresent() && assigneeOpt.isPresent()) {
            Task task = taskOpt.get();
            User assignee = assigneeOpt.get();

            createHistory(task, "assignedTo",
                    task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : "null",
                    assignee.getUsername(), userId);

            task.setAssignedTo(assignee);
            return ResponseEntity.ok(service.save(task));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private void createHistory(Task task, String field, String oldValue, String newValue, Long userId) {
        TaskHistory history = TaskHistory.builder()
                .task(task)
                .oldValue(field + " : " + oldValue)
                .newValue(newValue)
                .changeDate(LocalDateTime.now())
                .changedBy(userId != null ? userService.getById(userId).orElse(null) : null)
                .build();

        historyService.save(history);
    }

    private boolean hasChanged(Object oldVal, Object newVal) {
        if (newVal == null) return false; // <— do not treat null as an intentional change
        if (oldVal == null) return true;
        return !oldVal.equals(newVal);
    }


}
