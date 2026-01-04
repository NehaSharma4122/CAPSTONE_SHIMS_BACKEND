package com.claimmanager.service;


import java.util.List;

import com.claimmanager.entity.Claim;
import com.claimmanager.entity.ClaimStatus;
import com.claimmanager.request.ClaimRequestDTO;

public interface ClaimService {

    Claim submitClaimByCustomer(Long userId, Long policyId, ClaimRequestDTO request);

    Claim submitClaimByHospital(Long hospitalId, Long policyId, ClaimRequestDTO request);

    Claim updateClaimStatus(Long claimId, ClaimStatus status);

    List<Claim> getAllClaims();

    Claim getClaimById(Long claimId);

    List<Claim> getClaimsByUser(Long userId);

    List<Claim> getClaimsByStatus(ClaimStatus status);
    
    List<Claim> getClaimsByHospital(Long hospitalId);
}
