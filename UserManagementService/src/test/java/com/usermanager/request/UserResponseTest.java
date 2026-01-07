package com.usermanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void testGetterSetter() {

        UserResponse res = new UserResponse();

        res.setUserId(10L);
        res.setUsername("mike");
        res.setEmail("mike@test.com");
        res.setRole(Role.ROLE_ADMIN);

        assertThat(res.getUserId()).isEqualTo(10L);
        assertThat(res.getUsername()).isEqualTo("mike");
        assertThat(res.getEmail()).isEqualTo("mike@test.com");
        assertThat(res.getRole()).isEqualTo(Role.ROLE_ADMIN);

        assertThat(res.toString()).contains("mike");
    }

    @Test
    void testEqualsAndHashCode() {

        UserResponse a = new UserResponse();
        a.setUserId(1L);
        a.setUsername("sam");
        a.setEmail("s@test.com");
        a.setRole(Role.ROLE_AGENT);

        UserResponse b = new UserResponse();
        b.setUserId(1L);
        b.setUsername("sam");
        b.setEmail("s@test.com");
        b.setRole(Role.ROLE_AGENT);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
