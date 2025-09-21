package com.iscod.eval.pmtv2_backend.services;

import com.iscod.eval.pmtv2_backend.models.Task;
import com.iscod.eval.pmtv2_backend.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService service;

    @MockitoBean
    private TaskRepository repo;

    @Test
    void testGetAll() {
        Task task = Task.builder().id(1L).name("Backend").build();
        when(repo.findAll()).thenReturn(List.of(task));

        List<Task> tasks = service.getAll();

        assertEquals(1, tasks.size());
        assertEquals("Backend", tasks.get(0).getName());
    }

    @Test
    void testGetByIdFound() {
        Task task = Task.builder().id(1L).name("Backend").build();
        when(repo.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> found = service.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("Backend", found.get().getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> found = service.getById(99L);

        assertTrue(found.isEmpty());
    }

    @Test
    void testSave() {
        Task task = Task.builder().id(1L).name("Backend").build();
        when(repo.save(any(Task.class))).thenReturn(task);

        Task saved = service.save(task);

        assertNotNull(saved);
        assertEquals("Backend", saved.getName());
    }

    @Test
    void testDelete() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}
