package com.claimmanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseDTOTests {

    @Test
    void testUserDtoFields() {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(5L);
        dto.setName("Neha");
        dto.setEmail("neha@mail.com");
        dto.setRole("ROLE_CUSTOMER");

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getEmail()).isEqualTo("neha@mail.com");
        assertThat(dto.getRole()).isEqualTo("ROLE_CUSTOMER");
    }
}
