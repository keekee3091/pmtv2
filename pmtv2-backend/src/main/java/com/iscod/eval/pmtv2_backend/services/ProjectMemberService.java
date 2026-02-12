package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.ProjectMember;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.ProjectMemberRepository;
import com.iscod.eval.pmtv2_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectMemberService {
    private final ProjectMemberRepository repo;
    private final UserRepository userRepository;

    public ProjectMemberService(ProjectMemberRepository repo, UserRepository userRepository) { this.repo = repo;
        this.userRepository = userRepository;
    }

    public List<ProjectMember> getAll() { return repo.findAll(); }
    public Optional<ProjectMember> getById(Long id) { return repo.findById(id); }
    public ProjectMember save(ProjectMember pm) { return repo.save(pm); }
    public void delete(Long id) { repo.deleteById(id); }

    public List<User> getUsersByProjectId(Long projectId) {
        List<Long> ids = repo.findUserIdsByProjectId(projectId);
        return userRepository.findAllById(ids);
    }
}
