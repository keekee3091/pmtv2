package com.iscod.eval.pmtv2_backend.models;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserModelTest {

    @Test
    void testBuilderAndGetters() {
        User user = User.builder()
                .id(1L)
                .username("Alice")
                .email("alice@example.com")
                .password("secret")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getPassword()).isEqualTo("secret");
    }

    @Test
    void testSettersAndRelations() {
        User user = new User();
        user.setId(2L);
        user.setUsername("Bob");
        user.setEmail("bob@example.com");
        user.setPassword("pwd");

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getUsername()).isEqualTo("Bob");
        assertThat(user.getEmail()).isEqualTo("bob@example.com");

        // relations par défaut
        assertThat(user.getOwnedProjects()).isNotNull();
        assertThat(user.getProjectMemberships()).isNotNull();
        assertThat(user.getAssignedTasks()).isNotNull();
        assertThat(user.getTaskHistories()).isNotNull();
        assertThat(user.getNotifications()).isNotNull();
    }
}
