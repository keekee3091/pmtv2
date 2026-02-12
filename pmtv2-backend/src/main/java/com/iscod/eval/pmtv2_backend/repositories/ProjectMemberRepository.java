package com.iscod.eval.pmtv2_backend.repositories;

import com.iscod.eval.pmtv2_backend.models.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query(value = """
            SELECT user_id FROM project_members WHERE project_members.project_id = :projectId
            """, nativeQuery = true)
    List<Long> findUserIdsByProjectId(@Param("projectId") Long projectId);
}
