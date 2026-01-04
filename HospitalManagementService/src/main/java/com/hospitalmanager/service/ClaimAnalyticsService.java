package com.hospitalmanager.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hospitalmanager.request.ClaimStatsDTO;
import com.hospitalmanager.feign.ClaimFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClaimAnalyticsService {

    private final ClaimFeignClient claimClient;

    private ClaimStatsDTO enrichTotals(ClaimStatsDTO dto) {

        double total =
                (dto.getOperationCost() != null ? dto.getOperationCost() : 0) +
                (dto.getMedicineCost()  != null ? dto.getMedicineCost()  : 0) +
                (dto.getPostOpsCost()   != null ? dto.getPostOpsCost()   : 0);

        dto.setClaimAmount(BigDecimal.valueOf(total));

        dto.setApprovedAmount(dto.getClaimAmount());

        return dto;
    }

    public List<ClaimStatsDTO> getHospitalClaimsWithTotals(Long hospitalId) {

        return claimClient.getClaimsByHospital(hospitalId)
                .stream()
                .map(this::enrichTotals)
                .toList();
    }
}
