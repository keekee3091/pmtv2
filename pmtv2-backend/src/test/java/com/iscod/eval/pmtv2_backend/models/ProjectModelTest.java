package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectModelTest {

    @Test
    void testBuilderAndGetters() {
        User owner = User.builder().id(1L).username("Alice").build();

        Project project = Project.builder()
                .id(10L)
                .name("Project A")
                .description("Test project")
                .startDate(LocalDate.of(2025, 1, 1))
                .owner(owner)
                .build();

        assertThat(project.getId()).isEqualTo(10L);
        assertThat(project.getName()).isEqualTo("Project A");
        assertThat(project.getDescription()).isEqualTo("Test project");
        assertThat(project.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(project.getOwner()).isEqualTo(owner);
    }

    @Test
    void testSettersAndRelations() {
        Project project = new Project();
        project.setId(20L);
        project.setName("Project B");
        project.setDescription("Another project");
        project.setStartDate(LocalDate.of(2025, 2, 1));

        assertThat(project.getId()).isEqualTo(20L);
        assertThat(project.getName()).isEqualTo("Project B");
        assertThat(project.getDescription()).isEqualTo("Another project");
        assertThat(project.getStartDate()).isEqualTo(LocalDate.of(2025, 2, 1));

        // relations initialisées
        assertThat(project.getMembers()).isNotNull();
        assertThat(project.getTasks()).isNotNull();
    }
}
