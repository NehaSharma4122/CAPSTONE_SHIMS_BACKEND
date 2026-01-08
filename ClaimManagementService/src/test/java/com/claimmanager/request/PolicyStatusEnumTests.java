package com.claimmanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PolicyStatusEnumTests {

    @Test
    void testEnumValues() {
        assertThat(PolicyStatus.values()).hasSize(3);
        assertThat(PolicyStatus.valueOf("ACTIVE")).isEqualTo(PolicyStatus.ACTIVE);
        assertThat(PolicyStatus.valueOf("EXPIRED")).isEqualTo(PolicyStatus.EXPIRED);
        assertThat(PolicyStatus.valueOf("SUSPENDED")).isEqualTo(PolicyStatus.SUSPENDED);
    }
}
