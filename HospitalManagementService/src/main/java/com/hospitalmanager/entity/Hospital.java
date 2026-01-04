package com.hospitalmanager.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    // LOGIN USER ID == HOSPITAL ID
    @Id
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private boolean profileCompleted = false;

    private String name;
    private String city;
    private String state;
    private String address;
    private String licenseNumber;

    private String contactEmail;
    private String contactPhone;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<HospitalPlanLink> linkedPlans = new ArrayList<>();
}
