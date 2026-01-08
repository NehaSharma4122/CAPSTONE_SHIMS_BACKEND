package com.claimmanager.repository;

import com.claimmanager.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Forces H2 use
class ClaimRepositoryTests {

    @Autowired
    private ClaimRepository repository;

    @Test
    void testSaveAndFindByUserIdAndStatusAndHospitalId() {

        Claim claim = Claim.builder()
                .userId(5L)
                .policyId(1L)
                .hospitalId(9L)
                .diseaseType("Surgery")
                .status(ClaimStatus.SUBMITTED)
                .submittedAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        repository.save(claim);

        List<Claim> userClaims = repository.findByUserId(5L);
        assertThat(userClaims).hasSize(1);

        List<Claim> statusClaims = repository.findByStatus(ClaimStatus.SUBMITTED);
        assertThat(statusClaims).hasSize(1);

        List<Claim> hospitalClaims = repository.findByHospitalId(9L);
        assertThat(hospitalClaims).hasSize(1);
    }
}
