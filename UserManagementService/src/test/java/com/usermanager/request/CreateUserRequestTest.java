package com.usermanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUserRequestTest {

    @Test
    void testGettersSettersAndToString() {

        CreateUserRequest req = new CreateUserRequest();

        req.setUsername("john");
        req.setEmail("john@test.com");
        req.setPassword("pass123");
        req.setRole(Role.ROLE_AGENT);
        req.setOrganizationId("ORG1");

        assertThat(req.getUsername()).isEqualTo("john");
        assertThat(req.getEmail()).isEqualTo("john@test.com");
        assertThat(req.getPassword()).isEqualTo("pass123");
        assertThat(req.getRole()).isEqualTo(Role.ROLE_AGENT);
        assertThat(req.getOrganizationId()).isEqualTo("ORG1");

        assertThat(req.toString()).contains("john").contains("ORG1");
    }

    @Test
    void testEqualsAndHashCode() {

        CreateUserRequest a = new CreateUserRequest();
        a.setUsername("alex");
        a.setEmail("a@test.com");
        a.setPassword("x");
        a.setRole(Role.ROLE_HOSPITAL);
        a.setOrganizationId("O1");

        CreateUserRequest b = new CreateUserRequest();
        b.setUsername("alex");
        b.setEmail("a@test.com");
        b.setPassword("x");
        b.setRole(Role.ROLE_HOSPITAL);
        b.setOrganizationId("O1");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
