package com.hospitalmanager.request;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DtoCoverageTest {

    @Test
    void claimStatsDto_sets_fields() {

        ClaimStatsDTO dto = new ClaimStatsDTO();
        dto.setId(1L);
        dto.setClaimAmount(BigDecimal.TEN);

        assertEquals(1L, dto.getId());
        assertEquals(BigDecimal.TEN, dto.getClaimAmount());
    }

    @Test
    void hospitalDto_sets_fields() {

        HospitalDTO dto = new HospitalDTO();
        dto.setId(3L);
        dto.setName("Apollo");

        assertEquals(3L, dto.getId());
        assertEquals("Apollo", dto.getName());
    }

    @Test
    void hospitalPlanResponseDto_sets_fields() {

        HospitalPlanResponseDTO dto = new HospitalPlanResponseDTO();
        dto.setPlanId(5L);

        assertEquals(5L, dto.getPlanId());
    }

    @Test
    void onboardingRequest_sets_fields() {

        HospitalOnboardingRequest req = new HospitalOnboardingRequest();
        req.setName("Test");

        assertEquals("Test", req.getName());
    }
}
