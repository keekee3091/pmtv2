package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() { return repo.findAll(); }
    public Optional<User> getById(Long id) { return repo.findById(id); }
    public User save(User u) { return repo.save(u); }
    public void delete(Long id) { repo.deleteById(id); }
}
