package com.claimmanager.service;

import com.claimmanager.client.AuthUserClient;
import com.claimmanager.client.PolicyFeignClient;
import com.claimmanager.entity.*;
import com.claimmanager.exception.ClaimNotFoundException;
import com.claimmanager.exception.PolicyValidationException;
import com.claimmanager.repository.ClaimRepository;
import com.claimmanager.request.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimServiceImplTests {

    private ClaimRepository repo;
    private PolicyFeignClient policyClient;
    private EmailService emailService;
    private AuthUserClient authUserClient;
    private ClaimServiceImpl service;

    @BeforeEach
    void setup() {
        repo = mock(ClaimRepository.class);
        policyClient = mock(PolicyFeignClient.class);
        emailService = mock(EmailService.class);
        authUserClient = mock(AuthUserClient.class);
        service = new ClaimServiceImpl(repo, policyClient, emailService, authUserClient);
    }

    // 1. Coverage for submitClaimByCustomer (including Feign Forbidden Exception)
    @Test
    void testSubmitClaimByCustomerForbidden() {
        // First call (outside try) must succeed to reach the try-catch block
        // Second call (inside try) must throw Forbidden to hit the catch block
        PolicyResponseDTO policy = new PolicyResponseDTO();
        when(policyClient.getUserPolicy(1L, 2L))
            .thenReturn(policy) // First call succeeds
            .thenThrow(feign.FeignException.Forbidden.class); // Second call fails

        assertThatThrownBy(() -> service.submitClaimByCustomer(1L, 2L, new ClaimRequestDTO()))
                .isInstanceOf(PolicyValidationException.class)
                .hasMessageContaining("does not belong to user");
    }

    @Test
    void testSubmitClaimByHospital() {
        ClaimRequestDTO req = new ClaimRequestDTO();
        req.setDiseaseType("Surgery");
        req.setOperationCost(500.0);
        
        when(repo.save(any())).thenReturn(new Claim());

        Claim result = service.submitClaimByHospital(5L, 10L, req);
        assertThat(result).isNotNull();
        verify(repo).save(any());
    }

    @Test
    void testUpdateStatus_PaidCaseWithNulls() {
        // This hits calculateClaimAmount() and the PAID switch case
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setUserId(1L);
        claim.setStatus(ClaimStatus.PAID);
        claim.setOperationCost(1000.0);
        // Leaving medicine and post-ops null to hit the (cost != null ? cost : 0) logic

        UserResponseDTO user = new UserResponseDTO();
        user.setEmail("test@mail.com");

        when(repo.findById(1L)).thenReturn(Optional.of(claim));
        when(authUserClient.getUserById(1L)).thenReturn(user);

        service.updateClaimStatus(1L, ClaimStatus.PAID);
        
        verify(emailService).sendMail(any());
    }

    @Test
    void testSubmitClaimByCustomerSuccess() {
        ClaimRequestDTO req = new ClaimRequestDTO();
        req.setDiseaseType("Surgery");
        
        PolicyResponseDTO policy = new PolicyResponseDTO();
        when(policyClient.getUserPolicy(1L, 2L)).thenReturn(policy);
        when(repo.save(any())).thenReturn(new Claim());

        service.submitClaimByCustomer(1L, 2L, req);
        verify(repo).save(any());
    }

    // 2. Coverage for submitClaimByHospital (0% in your current report)

    // 3. FULL EMAIL COVERAGE (Hits all Switch Cases & calculateClaimAmount)
    @Test
    void testUpdateStatusFullEmailFlow() {
        // Setup a claim with costs to trigger calculateClaimAmount null checks
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setUserId(1L);
        claim.setOperationCost(1000.0);
        claim.setMedicineCost(null); // Hits the (cost != null ? cost : 0) logic
        claim.setPostOpsCost(500.0);

        UserResponseDTO user = new UserResponseDTO();
        user.setEmail("test@mail.com");
        user.setName("Test User");

        when(repo.findById(1L)).thenReturn(Optional.of(claim));
        when(authUserClient.getUserById(1L)).thenReturn(user);

        // Call each status to hit every switch branch
        service.updateClaimStatus(1L, ClaimStatus.IN_REVIEW);
        service.updateClaimStatus(1L, ClaimStatus.APPROVED);
        service.updateClaimStatus(1L, ClaimStatus.REJECTED);
        service.updateClaimStatus(1L, ClaimStatus.PAID); // This hits calculateClaimAmount()

        verify(emailService, times(4)).sendMail(any());
    }

    @Test
    void testUpdateStatusUserEmailNotFound() {
        Claim claim = new Claim();
        claim.setUserId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(claim));
        
        // Triggers the "if (user == null || user.getEmail() == null)" branch
        when(authUserClient.getUserById(1L)).thenReturn(null);

        service.updateClaimStatus(1L, ClaimStatus.APPROVED);
        verify(emailService, never()).sendMail(any());
    }

    // 4. Coverage for GET and Exception paths
    @Test
    void testGetClaimByIdNotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getClaimById(1L)).isInstanceOf(ClaimNotFoundException.class);
    }

    @Test
    void testGetAllAndQueries() {
        when(repo.findAll()).thenReturn(List.of());
        service.getAllClaims();
        service.getClaimsByStatus(ClaimStatus.SUBMITTED);
        verify(repo).findAll();
        verify(repo).findByStatus(ClaimStatus.SUBMITTED);
    }
}