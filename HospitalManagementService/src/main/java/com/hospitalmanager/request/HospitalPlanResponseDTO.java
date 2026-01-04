package com.hospitalmanager.request;

import lombok.Data;

@Data
public class HospitalPlanResponseDTO {

    private Long id;
    private Long planId;
    private HospitalDTO hospital;
}
