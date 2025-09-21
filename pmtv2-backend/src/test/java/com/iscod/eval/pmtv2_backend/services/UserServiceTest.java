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
}
