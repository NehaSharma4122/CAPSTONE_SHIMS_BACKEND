package com.claimmanager.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimEntityTests {

    @Test
    void testClaimBuilderAndGetters() {

        LocalDateTime now = LocalDateTime.now();

        Claim claim = Claim.builder()
                .id(1L)
                .userId(10L)
                .policyId(20L)
                .hospitalId(30L)
                .diseaseType("Appendicitis")
                .operationCost(50000.0)
                .medicineCost(12000.0)
                .postOpsCost(8000.0)
                .status(ClaimStatus.SUBMITTED)
                .submittedAt(now)
                .lastUpdatedAt(now)
                .documents(List.of())
                .build();

        assertThat(claim.getId()).isEqualTo(1L);
        assertThat(claim.getUserId()).isEqualTo(10L);
        assertThat(claim.getPolicyId()).isEqualTo(20L);
        assertThat(claim.getHospitalId()).isEqualTo(30L);
        assertThat(claim.getDiseaseType()).isEqualTo("Appendicitis");
        assertThat(claim.getOperationCost()).isEqualTo(50000.0);
        assertThat(claim.getMedicineCost()).isEqualTo(12000.0);
        assertThat(claim.getPostOpsCost()).isEqualTo(8000.0);
        assertThat(claim.getStatus()).isEqualTo(ClaimStatus.SUBMITTED);
        assertThat(claim.getSubmittedAt()).isEqualTo(now);
        assertThat(claim.getLastUpdatedAt()).isEqualTo(now);
        assertThat(claim.getDocuments()).isEmpty();
    }

    @Test
    void testClaimNoArgsAndSetters() {

        Claim claim = new Claim();

        claim.setId(5L);
        claim.setUserId(1L);
        claim.setPolicyId(2L);
        claim.setHospitalId(3L);

        claim.setDiseaseType("Surgery");
        claim.setOperationCost(1000.0);

        assertThat(claim.getId()).isEqualTo(5L);
        assertThat(claim.getUserId()).isEqualTo(1L);
        assertThat(claim.getPolicyId()).isEqualTo(2L);
        assertThat(claim.getHospitalId()).isEqualTo(3L);
        assertThat(claim.getDiseaseType()).isEqualTo("Surgery");
        assertThat(claim.getOperationCost()).isEqualTo(1000.0);
    }
}
