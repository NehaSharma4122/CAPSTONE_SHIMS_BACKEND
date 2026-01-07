package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyStatusTest {

    @Test
    void verifyEnumValues() {
        assertEquals(4, PolicyStatus.values().length);
        assertEquals(PolicyStatus.PENDING_PAYMENT, PolicyStatus.valueOf("PENDING_PAYMENT"));
        assertEquals(PolicyStatus.ACTIVE, PolicyStatus.valueOf("ACTIVE"));
        assertEquals(PolicyStatus.EXPIRED, PolicyStatus.valueOf("EXPIRED"));
        assertEquals(PolicyStatus.SUSPENDED, PolicyStatus.valueOf("SUSPENDED"));
    }
}
