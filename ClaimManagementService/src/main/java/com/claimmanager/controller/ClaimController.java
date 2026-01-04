package com.claimmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.claimmanager.entity.Claim;
import com.claimmanager.entity.ClaimStatus;
import com.claimmanager.exception.InvalidClaimStatusException;
import com.claimmanager.request.ClaimRequestDTO;
import com.claimmanager.service.ClaimService;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    // CUSTOMER submits claim
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/users/{userId}/{policyId}")
    public Claim submitClaimByUser(
            @PathVariable Long userId,
            @PathVariable Long policyId,
            @RequestBody ClaimRequestDTO req) {
        return claimService.submitClaimByCustomer(userId, policyId, req);
    }

    // HOSPITAL submits claim
    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/hospitals/{hospitalId}/{policyId}")
    public Claim submitClaimByHospital(
            @PathVariable Long hospitalId,
            @PathVariable Long policyId,
            @RequestBody ClaimRequestDTO req) {
        return claimService.submitClaimByHospital(hospitalId, policyId, req);
    }

    // CLAIM OFFICER STATUS CHANGE
    @PreAuthorize("hasRole('CLAIMS_OFFICER')")
    @PutMapping("/claimoff/status/review/{claimId}")
    public Claim markReview(@PathVariable Long claimId) {
        return claimService.updateClaimStatus(claimId, ClaimStatus.IN_REVIEW);
    }

    @PreAuthorize("hasRole('CLAIMS_OFFICER')")
    @PutMapping("/claimoff/status/approved/{claimId}")
    public Claim markApproved(@PathVariable Long claimId) {
        return claimService.updateClaimStatus(claimId, ClaimStatus.APPROVED);
    }

    @PreAuthorize("hasRole('CLAIMS_OFFICER')")
    @PutMapping("/claimoff/status/paid/{claimId}")
    public Claim markPaid(@PathVariable Long claimId) {
        return claimService.updateClaimStatus(claimId, ClaimStatus.PAID);
    }

    @PreAuthorize("hasRole('CLAIMS_OFFICER')")
    @PutMapping("/claimoff/status/rejected/{claimId}")
    public Claim markRejected(@PathVariable Long claimId) {
        return claimService.updateClaimStatus(claimId, ClaimStatus.REJECTED);
    }

    // ===== GET APIs =====

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('CLAIMS_OFFICER','CUSTOMER','HOSPITAL')")
    public List<Claim> getAllClaims() {
        return claimService.getAllClaims();
    }

    @GetMapping("/{claimId}")
    @PreAuthorize("hasAnyRole('CLAIMS_OFFICER','CUSTOMER','HOSPITAL')")
    public Claim getClaim(@PathVariable Long claimId) {
        return claimService.getClaimById(claimId);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('CLAIMS_OFFICER','CUSTOMER','HOSPITAL')")
    public List<Claim> getClaimsByUser(@PathVariable Long userId) {
        return claimService.getClaimsByUser(userId);
    }

    @GetMapping("/status/{status}/all")
    @PreAuthorize("hasRole('CLAIMS_OFFICER')")
    public List<Claim> getClaimsByStatus(@PathVariable String status) {
    	try {
            ClaimStatus claimStatus =
                    ClaimStatus.valueOf(status.toUpperCase());

            return claimService.getClaimsByStatus(claimStatus);

        } catch (IllegalArgumentException ex) {
            throw new InvalidClaimStatusException("Invalid claim status: " + status);
        }
    }
    
    @GetMapping("/hospital/{hospitalId}")
    @PreAuthorize("hasAnyRole('CLAIMS_OFFICER','HOSPITAL','ADMIN')")
    public List<Claim> getClaimsByHospital(@PathVariable Long hospitalId) {
        return claimService.getClaimsByHospital(hospitalId);
    }

}
