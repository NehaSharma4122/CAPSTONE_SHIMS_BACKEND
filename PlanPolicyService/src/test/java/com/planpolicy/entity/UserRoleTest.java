package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void verifyEnumValues() {
        assertEquals(5, UserRole.values().length);
        assertEquals(UserRole.ROLE_ADMIN, UserRole.valueOf("ROLE_ADMIN"));
        assertEquals(UserRole.ROLE_AGENT, UserRole.valueOf("ROLE_AGENT"));
        assertEquals(UserRole.ROLE_CUSTOMER, UserRole.valueOf("ROLE_CUSTOMER"));
    }
}
