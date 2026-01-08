package com.planpolicy.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PolicyTest {

    @Test
    void policy_DefaultStatus_IsPendingPayment() {
        Policy policy = new Policy();
        assertEquals(PolicyStatus.PENDING_PAYMENT, policy.getPolicyStatus());
        assertEquals(0, policy.getRenewalCount());
    }

    @Test
    void policy_GettersAndSetters() {
        InsurancePlan plan = new InsurancePlan();
        Policy p = new Policy();
        
        p.setId(1L);
        p.setUserId(100L);
        p.setPlan(plan);
        p.setStartDate(LocalDate.now());
        p.setExpiryDate(LocalDate.now().plusDays(1));
        p.setRenewalCount(1);
        p.setPremiumPaid(500.0);
        p.setPolicyStatus(PolicyStatus.ACTIVE);

        assertEquals(1L, p.getId());
        assertEquals(100L, p.getUserId());
        assertEquals(plan, p.getPlan());
        assertEquals(1, p.getRenewalCount());
        assertEquals(500.0, p.getPremiumPaid());
        assertEquals(PolicyStatus.ACTIVE, p.getPolicyStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        Policy p1 = new Policy();
        p1.setId(1L);
        
        Policy p2 = new Policy();
        p2.setId(1L);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, new Policy());
        
        // canEqual test (Lombok specific)
        assertTrue(p1.canEqual(p2));
    }

    @Test
    void testToString() {
        Policy p = new Policy();
        p.setId(99L);
        assertTrue(p.toString().contains("id=99"));
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
    void policy_FullEqualityCoverage() {
        Policy p1 = new Policy();
        p1.setId(1L);
        p1.setUserId(10L);
        p1.setPremiumPaid(100.0);
        p1.setRenewalCount(2);
        p1.setPolicyStatus(PolicyStatus.ACTIVE);
        p1.setStartDate(LocalDate.of(2023, 1, 1));
        p1.setExpiryDate(LocalDate.of(2024, 1, 1));
        
        Policy p2 = new Policy(); // Identical to p1
        p2.setId(1L);
        p2.setUserId(10L);
        p2.setPremiumPaid(100.0);
        p2.setRenewalCount(2);
        p2.setPolicyStatus(PolicyStatus.ACTIVE);
        p2.setStartDate(LocalDate.of(2023, 1, 1));
        p2.setExpiryDate(LocalDate.of(2024, 1, 1));

        // Test each field for inequality to trigger all Lombok branches
        p2.setId(2L);
        assertNotEquals(p1, p2);
        p2.setId(1L); // Reset

        p2.setUserId(20L);
        assertNotEquals(p1, p2);
        p2.setUserId(10L);

        p2.setPremiumPaid(200.0);
        assertNotEquals(p1, p2);
        p2.setPremiumPaid(100.0);

        p2.setRenewalCount(5);
        assertNotEquals(p1, p2);
        p2.setRenewalCount(2);

        p2.setPolicyStatus(PolicyStatus.EXPIRED);
        assertNotEquals(p1, p2);
        p2.setPolicyStatus(PolicyStatus.ACTIVE);

        p2.setStartDate(LocalDate.now());
        assertNotEquals(p1, p2);
        p2.setStartDate(LocalDate.of(2023, 1, 1));

        p2.setExpiryDate(LocalDate.now());
        assertNotEquals(p1, p2);
        
        // Test for null fields in hashCode and equals
        p1.setStartDate(null);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }
    

    
    
}