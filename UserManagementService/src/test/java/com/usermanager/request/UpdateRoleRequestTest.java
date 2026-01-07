package com.usermanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRoleRequestTest {

    @Test
    void testGetterSetter() {

        UpdateRoleRequest req = new UpdateRoleRequest();
        req.setRole(Role.ROLE_ADMIN);

        assertThat(req.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(req.toString()).contains("ROLE_ADMIN");
    }

    @Test
    void testEqualsAndHashCode() {

        UpdateRoleRequest a = new UpdateRoleRequest();
        a.setRole(Role.ROLE_AGENT);

        UpdateRoleRequest b = new UpdateRoleRequest();
        b.setRole(Role.ROLE_AGENT);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
