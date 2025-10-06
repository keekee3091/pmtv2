package com.iscod.eval.pmtv2_backend.repositories;

import com.iscod.eval.pmtv2_backend.models.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTaskId(Long taskId);
}
