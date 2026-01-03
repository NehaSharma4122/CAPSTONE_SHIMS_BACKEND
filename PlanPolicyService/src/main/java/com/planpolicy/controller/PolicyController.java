package com.planpolicy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planpolicy.entity.Policy;
import com.planpolicy.repository.PolicyRepository;
import com.planpolicy.service.PolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService service;
    private final PolicyRepository repo;
    
    @PreAuthorize("hasAnyRole('CUSTOMER','AGENT','ADMIN')")
    @GetMapping("/policy/inventory/users/{userId}")
    public List<Policy> getUserPolicies(@PathVariable Long userId) {
        return repo.findByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('AGENT','CUSTOMER')")
    @PostMapping("/policy/enroll/{userId}/{planId}")
    public Policy enroll(
            @PathVariable Long userId,
            @PathVariable Long planId) {

        return service.enrollPolicy(userId, planId);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','AGENT','ADMIN')")
    @PutMapping("/policy/users/renew/{userId}/{policyId}")
    public Policy renew(
            @PathVariable Long userId,
            @PathVariable Long policyId) {

        return service.renewPolicy(userId, policyId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @PutMapping("/policy/users/status/{userId}/{policyId}")
    public Policy suspend(
            @PathVariable Long userId,
            @PathVariable Long policyId) {

        return service.suspendPolicy(userId, policyId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/policy/{policyId}")
    public ResponseEntity<String> deletePolicy(
            @PathVariable Long policyId) {
        service.deletePolicy(policyId);
        return ResponseEntity.ok("Policy "+policyId+" for user has been successfully deleted.");
        
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/policy/all")
    public List<Policy> getAllPolicies() {
        return repo.findAll();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/policy/details/{policyId}")
    public Policy getPolicyById(@PathVariable Long policyId) {
        return service.getPolicy(policyId);
    }

}

