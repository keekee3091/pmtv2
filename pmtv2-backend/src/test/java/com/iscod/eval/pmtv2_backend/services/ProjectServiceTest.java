package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Project;
import com.iscod.eval.pmtv2_backend.repositories.ProjectRepository;
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
class ProjectServiceTest {

    @Mock
    private ProjectRepository repo;

    @InjectMocks
    private ProjectService service;

    @Test
    void shouldReturnAllProjects() {
        Project project = Project.builder().id(1L).name("P1").build();
        when(repo.findAll()).thenReturn(List.of(project));

        List<Project> result = service.getAll();

        assertThat(result).hasSize(1).first().extracting(Project::getName).isEqualTo("P1");
    }

    @Test
    void shouldReturnProjectById() {
        Project project = Project.builder().id(1L).name("P1").build();
        when(repo.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = service.getById(1L);

        assertThat(result).isPresent().get().extracting(Project::getName).isEqualTo("P1");
    }

    @Test
    void shouldSaveProject() {
        Project project = Project.builder().id(1L).name("P1").build();
        when(repo.save(project)).thenReturn(project);

        Project result = service.save(project);

        assertThat(result.getName()).isEqualTo("P1");
    }

    @Test
    void shouldDeleteProject() {
        service.delete(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}
