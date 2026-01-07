package com.planpolicy.repository;

import com.planpolicy.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PolicyRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PolicyRepository repo;

    @Test
    void savePolicy_AndRetrieveByUserId() {

        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName("Health Prime");
        plan.setPremiumAmount(15000.0);
        plan.setCoverageLimitType(CoverageLimitType.AMOUNT);
        plan.setCoverageLimitValue(50000.0);
        em.persist(plan);

        Policy policy = new Policy();
        policy.setUserId(99L);
        policy.setPlan(plan);
        policy.setPremiumPaid(15000.0);
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setStartDate(LocalDate.now());
        policy.setExpiryDate(LocalDate.now().plusYears(1));

        repo.save(policy);

        List<Policy> result = repo.findByUserId(99L);

        assertEquals(1, result.size());
        assertEquals(PolicyStatus.ACTIVE, result.get(0).getPolicyStatus());
    }

    @Test
    void existsByUserIdAndPlanIdAndStatus_Works() {

        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName("Shield Plan");
        plan.setPremiumAmount(10000.0);
        em.persist(plan);

        Policy p = new Policy();
        p.setUserId(50L);
        p.setPlan(plan);
        p.setPolicyStatus(PolicyStatus.ACTIVE);
        em.persist(p);

        boolean exists = repo.existsByUserIdAndPlanIdAndPolicyStatus(
                50L,
                plan.getId(),
                PolicyStatus.ACTIVE
        );

        assertTrue(exists);
    }
}
