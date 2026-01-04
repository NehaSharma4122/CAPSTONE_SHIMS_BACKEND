package com.claimmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long policyId;
    private Long hospitalId;

    private String diseaseType;
    private Double operationCost;
    private Double medicineCost;
    private Double postOpsCost;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime lastUpdatedAt;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimDocument> documents;
}

