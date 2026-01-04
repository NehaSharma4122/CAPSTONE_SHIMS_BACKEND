package com.claimmanager.request;

import java.time.LocalDate;

import lombok.Data;


@Data
public class PolicyResponseDTO {

    private Long id;
    private Long userId;

    private PolicyStatus policyStatus;

    private LocalDate startDate;
    private LocalDate expiryDate;

    private Integer renewalCount;
}
