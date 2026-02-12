package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectMemberModelTest {

    @Test
    void testBuilderAndGetters() {
        User user = User.builder().id(1L).username("Alice").build();
        Project project = Project.builder().id(1L).name("Project A").build();

        ProjectMember pm = ProjectMember.builder()
                .id(1L)
                .user(user)
                .project(project)
                .role("ADMIN")
                .build();

        assertThat(pm.getId()).isEqualTo(1L);
        assertThat(pm.getUser()).isEqualTo(user);
        assertThat(pm.getProject()).isEqualTo(project);
        assertThat(pm.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void testSetters() {
        ProjectMember pm = new ProjectMember();
        pm.setId(2L);
        pm.setRole("OBSERVER");

        assertThat(pm.getId()).isEqualTo(2L);
        assertThat(pm.getRole()).isEqualTo("OBSERVER");
    }
}
