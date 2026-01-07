package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoverageLimitTypeTest {

    @Test
    void verifyEnumValues() {
        assertEquals(2, CoverageLimitType.values().length);
        assertEquals(CoverageLimitType.PERCENT, CoverageLimitType.valueOf("PERCENT"));
        assertEquals(CoverageLimitType.AMOUNT, CoverageLimitType.valueOf("AMOUNT"));
    }
}
