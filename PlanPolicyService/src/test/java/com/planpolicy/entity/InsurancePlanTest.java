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

    @Test
    void testEqualsAndHashCode() {
        InsurancePlan plan1 = new InsurancePlan();
        plan1.setId(1L);
        
        InsurancePlan plan2 = new InsurancePlan();
        plan2.setId(1L);

        InsurancePlan plan3 = new InsurancePlan();
        plan3.setId(2L);

        // Test equality
        assertEquals(plan1, plan2);
        assertNotEquals(plan1, plan3);
        assertNotEquals(null, plan1);
        assertEquals(plan1, plan1);
        assertNotEquals(plan1, new Object());

        // Test HashCode
        assertEquals(plan1.hashCode(), plan2.hashCode());
        assertNotEquals(plan1.hashCode(), plan3.hashCode());
    }

    @Test
    void testToString() {
        InsurancePlan plan = new InsurancePlan();
        plan.setId(1L);
        plan.setPlanName("Test Plan");
        String result = plan.toString();
        
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("planName=Test Plan"));
    }
    
    @Test
    void forceLombokEqualityBranches() {
        InsurancePlan plan1 = new InsurancePlan();
        plan1.setId(1L);
        plan1.setPlanName("Alpha");

        InsurancePlan plan2 = new InsurancePlan();
        plan2.setId(1L);
        plan2.setPlanName("Alpha");

        InsurancePlan planDifferentField = new InsurancePlan();
        planDifferentField.setId(1L);
        planDifferentField.setPlanName("Beta"); // Different field triggers a branch

        // Branch: Same object
        assertEquals(plan1, plan1);
        // Branch: Null check
        assertNotEquals(null, plan1);
        // Branch: Different class
        assertNotEquals("StringObject", plan1);
        // Branch: Different field values
        assertNotEquals(plan1, planDifferentField);
        // Hashcode branches
        assertEquals(plan1.hashCode(), plan2.hashCode());
        assertNotEquals(plan1.hashCode(), planDifferentField.hashCode());
    }
    
    @Test
    void insurancePlan_FullEqualityCoverage() {
        InsurancePlan plan1 = new InsurancePlan();
        plan1.setId(1L);
        plan1.setPlanName("Gold");
        plan1.setPremiumAmount(100.0);
        plan1.setCoverageLimitValue(5000.0);
        plan1.setStatus(PlanStatus.ACTIVE);

        InsurancePlan plan2 = new InsurancePlan();
        plan2.setId(1L);
        plan2.setPlanName("Gold");
        plan2.setPremiumAmount(100.0);
        plan2.setCoverageLimitValue(5000.0);
        plan2.setStatus(PlanStatus.ACTIVE);

        // Verify consistency
        assertEquals(plan1, plan2);

        // Toggle every field to hit all branches in the generated equals()
        plan2.setId(9L);
        assertNotEquals(plan1, plan2);
        plan2.setId(1L);

        plan2.setPlanName("Silver");
        assertNotEquals(plan1, plan2);
        plan2.setPlanName("Gold");

        plan2.setPremiumAmount(999.0);
        assertNotEquals(plan1, plan2);
        plan2.setPremiumAmount(100.0);

        plan2.setCoverageLimitValue(1.0);
        assertNotEquals(plan1, plan2);
        plan2.setCoverageLimitValue(5000.0);

        plan2.setStatus(PlanStatus.DISCONTINUED);
        assertNotEquals(plan1, plan2);
        
        // Coverage for null-handling in equals/hashCode
        plan1.setPlanName(null);
        assertNotEquals(plan1, plan2);
        assertNotEquals(plan1.hashCode(), plan2.hashCode());
    }
}