package com.planpolicy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planpolicy.entity.InsurancePlan;
import com.planpolicy.repository.InsurancePlanRepository;
import com.planpolicy.service.InsurancePlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InsurancePlanController {

    private final InsurancePlanService service;
    private final InsurancePlanRepository repo;

    @GetMapping("/plans/inventory")
    public List<InsurancePlan> getPlans() {
        return repo.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/plans/add")
    public InsurancePlan addPlan(@RequestBody InsurancePlan plan) {
        return service.addPlan(plan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/plans/update/{planId}")
    public InsurancePlan updatePlan(
            @PathVariable Long planId,
            @RequestBody InsurancePlan updated
    ) {
        InsurancePlan plan = repo.findById(planId)
                .orElseThrow();

        plan.setPlanName(updated.getPlanName());
        plan.setPremiumAmount(updated.getPremiumAmount());
        plan.setCoverageLimitType(updated.getCoverageLimitType());
        plan.setCoverageLimitValue(updated.getCoverageLimitValue());

        service.validateCoverageRules(plan);
        service.computeCoverageCap(plan);

        return repo.save(plan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/plans/cancel/{planId}")
    public ResponseEntity<String> cancelPlan(@PathVariable Long planId) {
    	if (!repo.existsById(planId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Plan with ID " + planId + " not found.");
        }
    	repo.deleteById(planId);
        return ResponseEntity.ok("Plan with ID " + planId + " has been successfully deleted.");
    }
}
