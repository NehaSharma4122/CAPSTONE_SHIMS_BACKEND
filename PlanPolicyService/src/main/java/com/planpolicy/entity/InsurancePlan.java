package com.planpolicy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "insurance_plans")
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planName;
    private String description;

    private Double premiumAmount;

    @Enumerated(EnumType.STRING)
    private CoverageLimitType coverageLimitType;

    private Double coverageLimitValue;

    private Double maxCoverageAmount;

    @Enumerated(EnumType.STRING)
    private PlanStatus status = PlanStatus.ACTIVE;
}

