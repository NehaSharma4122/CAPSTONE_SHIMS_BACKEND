package com.claimmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.claimmanager.client.PolicyFeignClient;
import com.claimmanager.entity.Claim;
import com.claimmanager.entity.ClaimStatus;
import com.claimmanager.exception.ClaimNotFoundException;
import com.claimmanager.exception.PolicyValidationException;
import com.claimmanager.repository.ClaimRepository;
import com.claimmanager.request.ClaimRequestDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyFeignClient policyClient;

    @Override
    public Claim submitClaimByCustomer(Long userId, Long policyId, ClaimRequestDTO req) {
    	var policy = policyClient.getUserPolicy(userId, policyId);
    	try {
            policy = policyClient.getUserPolicy(userId, policyId);

        } catch (feign.FeignException.Forbidden ex) {

            throw new PolicyValidationException(
                "Policy " + policyId + " does not belong to user " + userId
            );
        }
    	if (policy == null)
    	    throw new PolicyValidationException("Policy not found for user");

        Claim claim = Claim.builder()
                .userId(userId)
                .policyId(policyId)
                .diseaseType(req.getDiseaseType())
                .operationCost(req.getOperationCost())
                .medicineCost(req.getMedicineCost())
                .postOpsCost(req.getPostOpsCost())
                .status(ClaimStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        return claimRepository.save(claim);
    }

    @Override
    public Claim submitClaimByHospital(Long hospitalId, Long policyId, ClaimRequestDTO req) {

        Claim claim = Claim.builder()
                .hospitalId(hospitalId)
                .policyId(policyId)
                .diseaseType(req.getDiseaseType())
                .operationCost(req.getOperationCost())
                .medicineCost(req.getMedicineCost())
                .postOpsCost(req.getPostOpsCost())
                .status(ClaimStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        return claimRepository.save(claim);
    }

    @Override
    public Claim updateClaimStatus(Long claimId, ClaimStatus status) {

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ClaimNotFoundException("Claim not found with id: " + claimId));

        claim.setStatus(status);
        claim.setLastUpdatedAt(LocalDateTime.now());

        return claimRepository.save(claim);
    }

    @Override
    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    @Override
    public Claim getClaimById(Long claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() ->
                        new ClaimNotFoundException("Claim not found with id: " + claimId));
    }

    @Override
    public List<Claim> getClaimsByUser(Long userId) {
        return claimRepository.findByUserId(userId);
    }

    @Override
    public List<Claim> getClaimsByStatus(ClaimStatus status) {
        return claimRepository.findByStatus(status);
    }
    
    @Override
    public List<Claim> getClaimsByHospital(Long hospitalId) {
        return claimRepository.findByHospitalId(hospitalId);
    }

}
