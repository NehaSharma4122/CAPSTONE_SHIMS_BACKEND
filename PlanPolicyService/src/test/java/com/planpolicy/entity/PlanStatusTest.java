package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlanStatusTest {

    @Test
    void verifyEnumValues() {
            assertEquals(2, PlanStatus.values().length);
            assertEquals(PlanStatus.ACTIVE, PlanStatus.valueOf("ACTIVE"));
            assertEquals(PlanStatus.DISCONTINUED, PlanStatus.valueOf("DISCONTINUED"));
    }
}
