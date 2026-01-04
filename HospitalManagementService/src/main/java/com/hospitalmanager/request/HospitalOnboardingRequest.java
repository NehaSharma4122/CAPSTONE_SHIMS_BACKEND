package com.hospitalmanager.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HospitalOnboardingRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    private String address;

    @NotBlank
    private String licenseNumber;

    @Email
    private String contactEmail;

    @NotBlank
    private String contactPhone;
}
