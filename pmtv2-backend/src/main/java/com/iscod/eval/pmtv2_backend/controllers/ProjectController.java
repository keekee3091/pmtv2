package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;

import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.ProjectRepository;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import com.iscod.eval.pmtv2_backend.services.ProjectService;

import com.iscod.eval.pmtv2_backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;
    private final UserService userService;

    public ProjectController(ProjectService service,
                             ProjectRepository projectRepository,
                             TaskRepository taskRepository, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    public record ProjectDTO(
            String name,
            String description,
            LocalDate startDate,
            Long ownerId
    ) {}

    @GetMapping
    public List<Project> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody ProjectDTO dto) {
        User owner = userService.getById(dto.ownerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Project project = Project.builder()
                .name(dto.name())
                .description(dto.description())
                .startDate(dto.startDate())
                .owner(owner)
                .build();

        return ResponseEntity.ok(service.save(project));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody Project project) {
        return service.getById(id)
                .map(existing -> {
                    project.setId(id);
                    return ResponseEntity.ok(service.save(project));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
