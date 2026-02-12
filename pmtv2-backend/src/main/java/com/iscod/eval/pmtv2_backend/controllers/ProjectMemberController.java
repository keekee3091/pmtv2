package com.iscod.eval.pmtv2_backend.controllers;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.models.ProjectMember;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.services.ProjectMemberService;
import com.iscod.eval.pmtv2_backend.services.ProjectService;
import com.iscod.eval.pmtv2_backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService service;
    private final UserService userService;
    private final ProjectService projectService;

    public ProjectMemberController(ProjectMemberService service, UserService userService, ProjectService projectService) {
        this.service = service;
        this.userService = userService;
        this.projectService = projectService;
    }

    public record ProjectMemberDTO(
            Long user,
            Long project,
            String role
    ) {}

    @GetMapping
    public List<ProjectMember> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectMember> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<User>> getUsersByProject(@PathVariable Long projectId) {
        List<User> users = service.getUsersByProjectId(projectId);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<ProjectMember> create(@RequestBody ProjectMemberDTO dto) {
        User user = userService.getById(dto.user())
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Project project = projectService.getById(dto.project())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        ProjectMember projectMember = ProjectMember.builder()
                .user(user)
                .project(project)
                .role(dto.role())
                .build();

        return ResponseEntity.ok(service.save(projectMember));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectMember> update(@PathVariable Long id, @RequestBody ProjectMember member) {
        return service.getById(id)
                .map(existing -> {
                    member.setId(id);
                    return ResponseEntity.ok(service.save(member));
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
