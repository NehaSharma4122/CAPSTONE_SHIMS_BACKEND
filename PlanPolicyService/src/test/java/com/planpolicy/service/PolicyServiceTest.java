package com.planpolicy.service;

import com.planpolicy.client.AuthUserClient;
import com.planpolicy.entity.*;
import com.planpolicy.repository.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepo;

    @Mock
    private InsurancePlanRepository planRepo;

    @Mock
    private AuthUserClient authUserClient;

    @InjectMocks
    private PolicyService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- ROLE + USER MOCKER ----------

    private void mockUser(long id, String role) {

        var auth = mock(org.springframework.security.core.Authentication.class);

        doReturn(String.valueOf(id)).when(auth).getPrincipal();
        doReturn(
                java.util.List.of(
                        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
                )
        ).when(auth).getAuthorities();

        var ctx = mock(org.springframework.security.core.context.SecurityContext.class);
        doReturn(auth).when(ctx).getAuthentication();

        org.springframework.security.core.context.SecurityContextHolder.setContext(ctx);
    }

    private InsurancePlan samplePlan() {
        InsurancePlan p = new InsurancePlan();
        p.setPremiumAmount(10000.0);
        return p;
    }

    // ---------- ENROLL ----------

    @Test
    void enrollPolicy_Succeeds_ForCustomer() {

        mockUser(5L, "CUSTOMER");

        when(authUserClient.getUserById(5L))
                .thenReturn(new com.planpolicy.request.UserDetailsRequest(
                        5L, "u", "e", "ROLE_CUSTOMER"));

        when(policyRepo.existsByUserIdAndPlanIdAndPolicyStatus(5L, 1L, PolicyStatus.ACTIVE))
                .thenReturn(false);

        when(planRepo.findById(1L)).thenReturn(Optional.of(samplePlan()));

        when(policyRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Policy p = service.enrollPolicy(5L, 1L);

        assertEquals(PolicyStatus.PENDING_PAYMENT, p.getPolicyStatus());
        assertNull(p.getStartDate());
        assertEquals(10000.0, p.getPremiumPaid());
    }

    @Test
    void enrollPolicy_Throws_WhenDuplicateActiveExists() {

        mockUser(5L, "CUSTOMER");

        when(authUserClient.getUserById(5L))
                .thenReturn(new com.planpolicy.request.UserDetailsRequest(
                        5L, "u", "e", "ROLE_CUSTOMER"));

        when(policyRepo.existsByUserIdAndPlanIdAndPolicyStatus(5L, 1L, PolicyStatus.ACTIVE))
                .thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> service.enrollPolicy(5L, 1L));
    }

    @Test
    void enrollPolicy_Throws_WhenTargetNotCustomer() {

        mockUser(5L, "AGENT");

        when(authUserClient.getUserById(5L))
                .thenReturn(new com.planpolicy.request.UserDetailsRequest(
                        5L, "u", "e", "ROLE_AGENT"));

        assertThrows(RuntimeException.class,
                () -> service.enrollPolicy(5L, 1L));
    }

    // ---------- RENEW ----------

    @Test
    void renewPolicy_SetsPendingPayment() {

        mockUser(5L, "CUSTOMER");

        Policy p = new Policy();
        p.setPlan(samplePlan());
        p.setPolicyStatus(PolicyStatus.ACTIVE);

        when(policyRepo.findById(3L)).thenReturn(Optional.of(p));
        when(policyRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Policy result = service.renewPolicy(5L, 3L);

        assertEquals(PolicyStatus.PENDING_PAYMENT, result.getPolicyStatus());
    }

    @Test
    void renewPolicy_Throws_WhenSuspended() {

        mockUser(5L, "CUSTOMER");

        Policy p = new Policy();
        p.setPolicyStatus(PolicyStatus.SUSPENDED);

        when(policyRepo.findById(3L)).thenReturn(Optional.of(p));

        assertThrows(RuntimeException.class,
                () -> service.renewPolicy(5L, 3L));
    }

    // ---------- ACTIVATE AFTER PAYMENT ----------

    @Test
    void activatePolicy_FirstTimeEnrollment_SetsStartDate() {

        Policy p = new Policy();
        p.setPolicyStatus(PolicyStatus.PENDING_PAYMENT);

        when(policyRepo.findById(2L)).thenReturn(Optional.of(p));
        when(policyRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Policy result = service.activatePolicyAfterPayment(2L);

        assertEquals(PolicyStatus.ACTIVE, result.getPolicyStatus());
        assertNotNull(result.getStartDate());
        assertEquals(LocalDate.now().plusYears(1), result.getExpiryDate());
    }

    @Test
    void activatePolicy_Renewal_IncrementsYears() {

        Policy p = new Policy();
        p.setPolicyStatus(PolicyStatus.PENDING_PAYMENT);
        p.setStartDate(LocalDate.now().minusYears(1));
        p.setExpiryDate(LocalDate.now());
        p.setRenewalCount(1);

        when(policyRepo.findById(2L)).thenReturn(Optional.of(p));
        when(policyRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Policy result = service.activatePolicyAfterPayment(2L);

        assertEquals(2, result.getRenewalCount());
    }

    // ---------- SUSPEND ----------

    @Test
    void suspendPolicy_SetsSuspendedStatus() {

        mockUser(5L, "CUSTOMER");

        Policy p = new Policy();
        p.setUserId(5L);

        when(policyRepo.findById(7L)).thenReturn(Optional.of(p));
        when(policyRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Policy result = service.suspendPolicy(5L, 7L);

        assertEquals(PolicyStatus.SUSPENDED, result.getPolicyStatus());
    }

    // ---------- ACTIVATE FROM SUSPENDED ----------

    @Test
    void activatePolicy_Throws_WhenNotSuspended() {

        mockUser(5L, "CUSTOMER");

        Policy p = new Policy();
        p.setUserId(5L);
        p.setPolicyStatus(PolicyStatus.ACTIVE);

        when(policyRepo.findById(7L)).thenReturn(Optional.of(p));

        assertThrows(RuntimeException.class,
                () -> service.activatePolicy(5L, 7L));
    }

    // ---------- DELETE ----------

    @Test
    void deletePolicy_Throws_WhenNotAdmin() {

        mockUser(5L, "CUSTOMER");

        assertThrows(Exception.class,
                () -> service.deletePolicy(9L));
    }

    @Test
    void deletePolicy_Succeeds_ForAdmin() {

        mockUser(1L, "ADMIN");

        service.deletePolicy(9L);

        verify(policyRepo).deleteById(9L);
    }
}
