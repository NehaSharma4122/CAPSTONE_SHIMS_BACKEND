package com.claimmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.claimmanager.client.AuthUserClient;
import com.claimmanager.client.PolicyFeignClient;
import com.claimmanager.entity.Claim;
import com.claimmanager.entity.ClaimStatus;
import com.claimmanager.exception.ClaimNotFoundException;
import com.claimmanager.exception.PolicyValidationException;
import com.claimmanager.repository.ClaimRepository;
import com.claimmanager.request.ClaimRequestDTO;
import com.claimmanager.request.EmailDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyFeignClient policyClient;
    private final EmailService emailService;
    private final AuthUserClient authUserClient;
    
    private double calculateClaimAmount(Claim claim) {
        return (claim.getOperationCost() != null ? claim.getOperationCost() : 0)
             + (claim.getMedicineCost() != null ? claim.getMedicineCost() : 0)
             + (claim.getPostOpsCost() != null ? claim.getPostOpsCost() : 0);
    }

    private void sendClaimStatusEmail(Claim claim) {

    	var user = authUserClient.getUserById(claim.getUserId());

        if (user == null || user.getEmail() == null) {
            System.out.println("⚠ Unable to fetch user email for userId = " + claim.getUserId());
            return;
        }

        String userEmail = user.getEmail();
        String name = user.getName() != null ? user.getName() : "Customer";

        switch (claim.getStatus()) {

            case IN_REVIEW ->
                    emailService.sendMail(
                            EmailDetails.builder()
                                    .to(userEmail)
                                    .subject("Your Insurance Claim is Under Review")
                                    .body(
                                            "Dear Policy Holder,\n\n" +
                                            "Your claim (ID: " + claim.getId() + ") is now under review.\n" +
                                            "Our claims officer will check and update the status shortly.\n\n" +
                                            "Thank you,\nSmart Health Insurance"
                                    ).build()
                    );

            case APPROVED ->
                    emailService.sendMail(
                            EmailDetails.builder()
                                    .to(userEmail)
                                    .subject("Your Insurance Claim Has Been Approved")
                                    .body(
                                            "Great news!\n\n" +
                                            "Your claim (ID: " + claim.getId() + ") has been APPROVED.\n" +
                                            "The payout will be processed shortly.\n\n" +
                                            "Thank you,\nSmart Health Insurance"
                                    ).build()
                    );

            case REJECTED ->
                    emailService.sendMail(
                            EmailDetails.builder()
                                    .to(userEmail)
                                    .subject("Your Insurance Claim Has Been Rejected")
                                    .body(
                                            "We are sorry to inform you that your claim (ID: " + claim.getId() + ") " +
                                            "has been REJECTED.\n\n" +
                                            "For more details, please contact support or your insurance agent.\n\n" +
                                            "Thank you,\nSmart Health Insurance"
                                    ).build()
                    );

            case PAID -> {

                double claimAmount = calculateClaimAmount(claim);

                emailService.sendMail(
                        EmailDetails.builder()
                                .to(userEmail)
                                .subject("🎉 Claim Settlement Successful — Amount Credited")
                                .body(
                                        "Congratulations!\n\n" +
                                        "Your claim (ID: " + claim.getId() + ") has been successfully PAID.\n\n" +
                                        "Total Claim Amount Credited: ₹ " + claimAmount + "\n\n" +
                                        "Breakdown:\n" +
                                        "• Operation Cost: ₹ " + claim.getOperationCost() + "\n" +
                                        "• Medicine Cost: ₹ " + claim.getMedicineCost() + "\n" +
                                        "• Post-Ops Cost: ₹ " + claim.getPostOpsCost() + "\n\n" +
                                        "Thank you for trusting Smart Health Insurance.\n" +
                                        "We wish you a speedy recovery."
                                ).build()
                );
            }
        }
    }

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
        
        claimRepository.save(claim);

        sendClaimStatusEmail(claim);

        return claim;
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
