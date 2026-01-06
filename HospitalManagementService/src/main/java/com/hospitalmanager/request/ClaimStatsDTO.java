package com.hospitalmanager.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ClaimStatsDTO {

    private Long id;

    private Long userId;
    private Long policyId;
    private Long hospitalId;

    private String diseaseType;

    private Double operationCost;
    private Double medicineCost;
    private Double postOpsCost;
    private BigDecimal claimAmount;
    private BigDecimal approvedAmount;
    private String status;

    private String submittedAt;
    private String lastUpdatedAt;
}

