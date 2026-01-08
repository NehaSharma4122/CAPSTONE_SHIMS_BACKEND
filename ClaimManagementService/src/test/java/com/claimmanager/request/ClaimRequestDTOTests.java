package com.claimmanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimRequestDTOTests {

    @Test
    void testDTOFields() {

        ClaimRequestDTO dto = new ClaimRequestDTO();

        dto.setDiseaseType("Fracture");
        dto.setOperationCost(20000.0);
        dto.setMedicineCost(5000.0);
        dto.setPostOpsCost(3000.0);

        assertThat(dto.getDiseaseType()).isEqualTo("Fracture");
        assertThat(dto.getOperationCost()).isEqualTo(20000.0);
        assertThat(dto.getMedicineCost()).isEqualTo(5000.0);
        assertThat(dto.getPostOpsCost()).isEqualTo(3000.0);
    }
}
