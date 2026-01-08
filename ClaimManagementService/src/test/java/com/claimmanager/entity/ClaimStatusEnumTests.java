package com.claimmanager.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClaimStatusEnumTests {

    @Test
    void testEnumValues() {

        assertThat(ClaimStatus.valueOf("SUBMITTED")).isEqualTo(ClaimStatus.SUBMITTED);
        assertThat(ClaimStatus.valueOf("IN_REVIEW")).isEqualTo(ClaimStatus.IN_REVIEW);
        assertThat(ClaimStatus.valueOf("APPROVED")).isEqualTo(ClaimStatus.APPROVED);
        assertThat(ClaimStatus.valueOf("PAID")).isEqualTo(ClaimStatus.PAID);
        assertThat(ClaimStatus.valueOf("REJECTED")).isEqualTo(ClaimStatus.REJECTED);

        assertThat(ClaimStatus.values()).hasSize(5);
    }
}
