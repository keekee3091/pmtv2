package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnAllUsers() {
        User user = User.builder().id(1L).username("Alice").build();
        when(repo.findAll()).thenReturn(List.of(user));

        List<User> result = service.getAll();

        assertThat(result).hasSize(1).first().extracting(User::getUsername).isEqualTo("Alice");
    }

    @Test
    void shouldReturnUserById() {
        User user = User.builder().id(1L).username("Alice").build();
        when(repo.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = service.getById(1L);

        assertThat(result).isPresent().get().extracting(User::getUsername).isEqualTo("Alice");
    }

    @Test
    void shouldSaveUser() {
        User user = User.builder().id(1L).username("Alice").build();
        when(repo.save(user)).thenReturn(user);

        User result = service.save(user);

        assertThat(result.getUsername()).isEqualTo("Alice");
    }

    @Test
    void shouldDeleteUser() {
        service.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void shouldFindByEmail() {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(repo.findByEmail("alice@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = service.findByEmail("alice@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        when(repo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<User> result = service.findByEmail("unknown@example.com");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldCheckExistsByEmail() {
        when(repo.existsByEmail("alice@example.com")).thenReturn(true);
        when(repo.existsByEmail("unknown@example.com")).thenReturn(false);

        assertThat(service.existsByEmail("alice@example.com")).isTrue();
        assertThat(service.existsByEmail("unknown@example.com")).isFalse();
    }
}
