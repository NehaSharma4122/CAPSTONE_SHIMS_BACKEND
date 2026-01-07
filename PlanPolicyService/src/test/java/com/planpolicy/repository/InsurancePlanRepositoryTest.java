package com.planpolicy.repository;

import com.planpolicy.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InsurancePlanRepositoryTest {

    @Autowired
    private InsurancePlanRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSavePlan() {

        InsurancePlan p = new InsurancePlan();
        p.setPlanName("Health Plus");
        p.setDescription("Family coverage");
        p.setPremiumAmount(12000.0);
        p.setCoverageLimitType(CoverageLimitType.PERCENT);
        p.setCoverageLimitValue(200.0);

        InsurancePlan saved = repo.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPlanName()).isEqualTo("Health Plus");
    }
}
