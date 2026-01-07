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
    void policy_StoresValuesCorrectly() {

        InsurancePlan plan = new InsurancePlan();
        plan.setId(10L);

        Policy p = new Policy();
        p.setId(5L);
        p.setUserId(21L);
        p.setPlan(plan);
        p.setStartDate(LocalDate.now());
        p.setExpiryDate(LocalDate.now().plusYears(1));
        p.setRenewalCount(2);
        p.setPremiumPaid(18000.0);
        p.setPolicyStatus(PolicyStatus.ACTIVE);

        assertEquals(5L, p.getId());
        assertEquals(21L, p.getUserId());
        assertEquals(plan, p.getPlan());
        assertNotNull(p.getStartDate());
        assertNotNull(p.getExpiryDate());
        assertEquals(2, p.getRenewalCount());
        assertEquals(18000.0, p.getPremiumPaid());
        assertEquals(PolicyStatus.ACTIVE, p.getPolicyStatus());
    }
}
