package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InsurancePlanTest {

    @Test
    void insurancePlan_DefaultValuesAndSettersWork() {

        InsurancePlan p = new InsurancePlan();

        p.setId(1L);
        p.setPlanName("Family Plus");
        p.setDescription("Coverage plan");
        p.setPremiumAmount(15000.0);
        p.setCoverageLimitType(CoverageLimitType.AMOUNT);
        p.setCoverageLimitValue(50000.0);
        p.setMaxCoverageAmount(60000.0);
        p.setStatus(PlanStatus.ACTIVE);

        assertEquals(1L, p.getId());
        assertEquals("Family Plus", p.getPlanName());
        assertEquals("Coverage plan", p.getDescription());
        assertEquals(15000.0, p.getPremiumAmount());
        assertEquals(CoverageLimitType.AMOUNT, p.getCoverageLimitType());
        assertEquals(50000.0, p.getCoverageLimitValue());
        assertEquals(60000.0, p.getMaxCoverageAmount());
        assertEquals(PlanStatus.ACTIVE, p.getStatus());
    }
}
