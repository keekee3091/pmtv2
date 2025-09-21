package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repo;

    public ProjectService(ProjectRepository repo) { this.repo = repo; }

    public List<Project> getAll() { return repo.findAll(); }
    public Optional<Project> getById(Long id) { return repo.findById(id); }
    public Project save(Project p) { return repo.save(p); }
    public void delete(Long id) { repo.deleteById(id); }
}
