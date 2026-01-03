package com.planpolicy.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private InsurancePlan plan;

    private LocalDate startDate;
    private LocalDate expiryDate;

    private Integer renewalCount = 0;

    private Double premiumPaid;

    @Enumerated(EnumType.STRING)
    private PolicyStatus policyStatus = PolicyStatus.ACTIVE;
}

