package com.claimmanager.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PolicyResponseDTOTests {

    @Test
    void testDTOFields() {

        PolicyResponseDTO dto = new PolicyResponseDTO();

        dto.setId(1L);
        dto.setUserId(10L);
        dto.setPolicyStatus(PolicyStatus.ACTIVE);
        dto.setStartDate(LocalDate.now());
        dto.setExpiryDate(LocalDate.now().plusYears(1));
        dto.setRenewalCount(2);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(10L);
        assertThat(dto.getPolicyStatus()).isEqualTo(PolicyStatus.ACTIVE);
    }
}
