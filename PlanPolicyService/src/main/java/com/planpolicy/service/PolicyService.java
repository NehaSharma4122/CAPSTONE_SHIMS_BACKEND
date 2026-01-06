package com.planpolicy.service;

import java.time.LocalDate;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.planpolicy.client.AuthUserClient;
import com.planpolicy.entity.InsurancePlan;
import com.planpolicy.entity.Policy;
import com.planpolicy.entity.PolicyStatus;
import com.planpolicy.repository.InsurancePlanRepository;
import com.planpolicy.repository.PolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepo;
    private final InsurancePlanRepository planRepo;
    private final AuthUserClient authUserClient;


    private Long getAuthUserId() {
        return Long.parseLong(
            SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString()
        );
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_" + role));
    }

    private void authorizeUserAccess(Long userId) {

        Long authUserId = getAuthUserId();

        if (!authUserId.equals(userId)
                && !hasRole("ADMIN")
                && !hasRole("AGENT")
                && !hasRole("CUSTOMER")) {

            throw new AccessDeniedException("Unauthorized access to policy");
        }
    }
    private String getAuthUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }

    private void ensureTargetUserIsCustomer(Long userId) {

        var user = authUserClient.getUserById(userId);

        if (user == null)
            throw new RuntimeException("User not found in Authentication Service");

        if (!"ROLE_CUSTOMER".equals(user.role())) {
            throw new AccessDeniedException(
                "Policy can only be issued to CUSTOMER users. Found: " + user.role()
            );
        }
    }

    public Policy enrollPolicy(Long userId, Long planId) {

        if (!hasRole("AGENT") && !hasRole("CUSTOMER"))
            throw new AccessDeniedException("Only Agent/Customer can enroll policy");
        
        ensureTargetUserIsCustomer(userId);

        boolean alreadyEnrolled =
                policyRepo.existsByUserIdAndPlanIdAndPolicyStatus(
                        userId,
                        planId,
                        PolicyStatus.ACTIVE
                );

        if (alreadyEnrolled) {
            throw new RuntimeException(
                "User already has an active policy for this plan. Renewal is allowed, duplicate enrollment is not."
            );
        }
        InsurancePlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Policy policy = new Policy();

        policy.setUserId(userId);
        policy.setPlan(plan);

        policy.setStartDate(null);
        policy.setExpiryDate(null);
        policy.setRenewalCount(0);

        policy.setPremiumPaid(plan.getPremiumAmount());
        policy.setPolicyStatus(PolicyStatus.PENDING_PAYMENT);

        return policyRepo.save(policy);
    }

    public Policy renewPolicy(Long userId, Long policyId) {

        if (!hasRole("CUSTOMER") && !hasRole("AGENT"))
            throw new AccessDeniedException("Only Customer/Agent can suspend policies");

        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getPolicyStatus() == PolicyStatus.SUSPENDED)
            throw new RuntimeException("Suspended policy cannot be renewed");
//
         // policy.setExpiryDate(null);
          policy.setPolicyStatus(PolicyStatus.PENDING_PAYMENT);
          policy.setPremiumPaid(policy.getPlan().getPremiumAmount());
        return policyRepo.save(policy);
    }

    public Policy activatePolicyAfterPayment(Long policyId) {

        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getPolicyStatus() != PolicyStatus.PENDING_PAYMENT)
            throw new RuntimeException("Policy is not awaiting payment");

        // first-time enrollment
        if (policy.getStartDate() == null) {
            policy.setStartDate(LocalDate.now());
            policy.setExpiryDate(LocalDate.now().plusYears(1));
        }
        else {
            // renewal success
            policy.setExpiryDate(policy.getExpiryDate().plusYears(1));
            policy.setRenewalCount(policy.getRenewalCount() + 1);
        }

        policy.setPolicyStatus(PolicyStatus.ACTIVE);

        return policyRepo.save(policy);
    }

    public Policy suspendPolicy(Long userId, Long policyId) {

        authorizeUserAccess(userId);

        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        policy.setPolicyStatus(PolicyStatus.SUSPENDED);

        return policyRepo.save(policy);
    }
    
    public Policy activatePolicy(Long userId, Long policyId) {

        authorizeUserAccess(userId);

        Policy policy = policyRepo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        if (policy.getPolicyStatus() != PolicyStatus.SUSPENDED) {
            throw new RuntimeException(
                "Only suspended policies can be activated. Current status: "
                + policy.getPolicyStatus()
            );
        }

        policy.setPolicyStatus(PolicyStatus.ACTIVE);


        return policyRepo.save(policy);
    }


    public Policy getPolicy(Long policyId) {
        return policyRepo.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public void deletePolicy(Long policyId) {

        if (!hasRole("ADMIN"))
            throw new AccessDeniedException("Only Admin can delete policies");

        policyRepo.deleteById(policyId);
    }
}
