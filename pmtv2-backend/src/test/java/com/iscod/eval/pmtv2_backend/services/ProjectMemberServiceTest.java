package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.ProjectMember;
import com.iscod.eval.pmtv2_backend.models.User;
import com.iscod.eval.pmtv2_backend.repositories.ProjectMemberRepository;
import com.iscod.eval.pmtv2_backend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectMemberServiceTest {

    @Mock
    private ProjectMemberRepository repo;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectMemberService service;

    @Test
    void shouldReturnAllMembers() {
        ProjectMember pm = ProjectMember.builder().id(1L).role("ADMIN").build();
        when(repo.findAll()).thenReturn(List.of(pm));

        List<ProjectMember> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRole()).isEqualTo("ADMIN");
    }

    @Test
    void shouldReturnMemberById() {
        ProjectMember pm = ProjectMember.builder().id(1L).role("MEMBER").build();
        when(repo.findById(1L)).thenReturn(Optional.of(pm));

        Optional<ProjectMember> result = service.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getRole()).isEqualTo("MEMBER");
    }

    @Test
    void shouldReturnEmptyWhenMemberNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<ProjectMember> result = service.getById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSaveMember() {
        ProjectMember pm = ProjectMember.builder().id(1L).role("OBSERVER").build();
        when(repo.save(pm)).thenReturn(pm);

        ProjectMember result = service.save(pm);

        assertThat(result.getRole()).isEqualTo("OBSERVER");
    }

    @Test
    void shouldDeleteMember() {
        service.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void shouldGetUsersByProjectId() {
        User user = User.builder().id(1L).username("Alice").email("alice@example.com").build();
        when(repo.findUserIdsByProjectId(1L)).thenReturn(List.of(1L));
        when(userRepository.findAllById(List.of(1L))).thenReturn(List.of(user));

        List<User> result = service.getUsersByProjectId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("Alice");
    }
}
