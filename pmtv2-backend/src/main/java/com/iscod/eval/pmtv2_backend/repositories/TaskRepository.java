package com.iscod.eval.pmtv2_backend.repositories;

import com.iscod.eval.pmtv2_backend.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
