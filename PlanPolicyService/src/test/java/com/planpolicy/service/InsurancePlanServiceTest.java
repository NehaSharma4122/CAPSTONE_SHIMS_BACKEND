package com.planpolicy.service;

import com.planpolicy.entity.*;
import com.planpolicy.repository.InsurancePlanRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsurancePlanServiceTest {

    @Mock
    private InsurancePlanRepository repo;

    @InjectMocks
    private InsurancePlanService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- ROLE MOCKERS (NO ARGUMENTS) ----------

    private void mockAdmin() {
        var auth = mock(org.springframework.security.core.Authentication.class);

        doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN")
                )
        ).when(auth).getAuthorities();

        var ctx = mock(org.springframework.security.core.context.SecurityContext.class);
        doReturn(auth).when(ctx).getAuthentication();

        org.springframework.security.core.context.SecurityContextHolder.setContext(ctx);
    }

    private void mockCustomer() {
        var auth = mock(org.springframework.security.core.Authentication.class);

        doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_CUSTOMER")
                )
        ).when(auth).getAuthorities();

        var ctx = mock(org.springframework.security.core.context.SecurityContext.class);
        doReturn(auth).when(ctx).getAuthentication();

        org.springframework.security.core.context.SecurityContextHolder.setContext(ctx);
    }

    // ---------- SAMPLE PLANS ----------

    private InsurancePlan samplePercentPlan() {
        InsurancePlan p = new InsurancePlan();
        p.setPremiumAmount(10000.0);
        p.setCoverageLimitType(CoverageLimitType.PERCENT);
        p.setCoverageLimitValue(200.0);
        return p;
    }

    private InsurancePlan sampleAmountPlan() {
        InsurancePlan p = new InsurancePlan();
        p.setPremiumAmount(10000.0);
        p.setCoverageLimitType(CoverageLimitType.AMOUNT);
        p.setCoverageLimitValue(50000.0);
        return p;
    }

    // ---------- TESTS ----------

    @Test
    void addPlan_SetsCoverageCap_ForPercentPlan() {

        mockAdmin();

        InsurancePlan plan = samplePercentPlan();
        when(repo.save(plan)).thenReturn(plan);

        InsurancePlan saved = service.addPlan(plan);

        assertEquals(20000.0, saved.getMaxCoverageAmount());
        assertEquals(PlanStatus.ACTIVE, saved.getStatus());
    }

    @Test
    void addPlan_SetsCoverageCap_ForAmountPlan() {

        mockAdmin();

        InsurancePlan plan = sampleAmountPlan();
        when(repo.save(plan)).thenReturn(plan);

        InsurancePlan saved = service.addPlan(plan);

        assertEquals(50000.0, saved.getMaxCoverageAmount());
    }

    @Test
    void addPlan_Throws_WhenNonAdmin() {

        mockCustomer();

        assertThrows(Exception.class,
                () -> service.addPlan(samplePercentPlan()));
    }

    @Test
    void validateCoverageRules_Throws_WhenPremiumInvalid() {

        mockAdmin();

        InsurancePlan p = samplePercentPlan();
        p.setPremiumAmount(0.0);

        assertThrows(RuntimeException.class,
                () -> service.addPlan(p));
    }

    @Test
    void validateCoverageRules_Throws_WhenCoverageLessThanPremium() {

        mockAdmin();

        InsurancePlan p = sampleAmountPlan();
        p.setCoverageLimitValue(1000.0);

        assertThrows(RuntimeException.class,
                () -> service.addPlan(p));
    }

    @Test
    void updatePlan_RecomputesCoverageCap() {

        mockAdmin();

        InsurancePlan existing = samplePercentPlan();
        existing.setId(1L);

        InsurancePlan updated = samplePercentPlan();
        updated.setCoverageLimitValue(300.0);

        when(repo.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        InsurancePlan result = service.updatePlan(1L, updated);

        assertEquals(30000.0, result.getMaxCoverageAmount());
    }

    @Test
    void cancelPlan_SetsStatusToDiscontinued() {

        mockAdmin();

        InsurancePlan plan = sampleAmountPlan();
        plan.setId(2L);

        when(repo.findById(2L)).thenReturn(java.util.Optional.of(plan));

        service.cancelPlan(2L);

        assertEquals(PlanStatus.DISCONTINUED, plan.getStatus());
        verify(repo).save(plan);
    }

    @Test
    void exists_ReturnsRepositoryValue() {
        when(repo.existsById(9L)).thenReturn(true);
        assertTrue(service.exists(9L));
    }
}
