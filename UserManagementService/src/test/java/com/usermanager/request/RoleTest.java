package com.usermanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void testEnumValues() {

        assertThat(Role.valueOf("ROLE_ADMIN")).isEqualTo(Role.ROLE_ADMIN);

        assertThat(Role.values())
                .containsExactly(
                        Role.ROLE_ADMIN,
                        Role.ROLE_AGENT,
                        Role.ROLE_CLAIMS_OFFICER,
                        Role.ROLE_HOSPITAL,
                        Role.ROLE_CUSTOMER
                );
    }
}
