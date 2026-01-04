package com.hospitalmanager.request;

import lombok.Data;

@Data
public class HospitalDTO {
    private Long id;
    private String name;
    private String city;
    private String state;
}