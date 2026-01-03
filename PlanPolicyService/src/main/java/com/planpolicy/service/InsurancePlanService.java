package com.planpolicy.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.planpolicy.entity.CoverageLimitType;
import com.planpolicy.entity.InsurancePlan;
import com.planpolicy.entity.PlanStatus;
import com.planpolicy.repository.InsurancePlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InsurancePlanService {

    private final InsurancePlanRepository repo;

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals("ROLE_" + role));
    }

    private void requireAdminAccess() {
        if (!hasRole("ADMIN")) {
            throw new AccessDeniedException("Only Admin can manage insurance plans");
        }
    }

    public InsurancePlan addPlan(InsurancePlan plan) {

        requireAdminAccess();

        validateCoverageRules(plan);
        computeCoverageCap(plan);

        plan.setStatus(PlanStatus.ACTIVE);

        return repo.save(plan);
    }

    public InsurancePlan updatePlan(Long planId, InsurancePlan updated) {

        requireAdminAccess();

        InsurancePlan plan = repo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setPlanName(updated.getPlanName());
        plan.setDescription(updated.getDescription());
        plan.setPremiumAmount(updated.getPremiumAmount());
        plan.setCoverageLimitType(updated.getCoverageLimitType());
        plan.setCoverageLimitValue(updated.getCoverageLimitValue());

        validateCoverageRules(plan);
        computeCoverageCap(plan);

        return repo.save(plan);
    }

    public void cancelPlan(Long planId) {

        requireAdminAccess();

        InsurancePlan plan = repo.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan.setStatus(PlanStatus.DISCONTINUED);

        repo.save(plan);
    }


    public void validateCoverageRules(InsurancePlan plan) {

        if (plan.getPremiumAmount() == null || plan.getPremiumAmount() <= 0)
            throw new RuntimeException("Premium must be greater than zero");

        if (plan.getCoverageLimitValue() == null || plan.getCoverageLimitValue() <= 0)
            throw new RuntimeException("Coverage limit must be greater than zero");

        if (plan.getCoverageLimitType() == CoverageLimitType.PERCENT
                && plan.getCoverageLimitValue() < 100) {
            throw new RuntimeException("Coverage % must be >= 100");
        }

        if (plan.getCoverageLimitType() == CoverageLimitType.AMOUNT
                && plan.getCoverageLimitValue() < plan.getPremiumAmount()) {
            throw new RuntimeException("Coverage amount must be greater than premium");
        }
    }

    public void computeCoverageCap(InsurancePlan plan) {

        if (plan.getCoverageLimitType() == CoverageLimitType.PERCENT) {

            double cap = plan.getPremiumAmount() *
                    (plan.getCoverageLimitValue() / 100.0);

            plan.setMaxCoverageAmount(cap);
        }
        else {
            plan.setMaxCoverageAmount(plan.getCoverageLimitValue());
        }
    }
}
