package com.claimmanager.request;

import lombok.*;

@Data
public class ClaimRequestDTO {

    private String diseaseType;
    private Double operationCost;
    private Double medicineCost;
    private Double postOpsCost;
}
