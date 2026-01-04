package com.hospitalmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_plan_links")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalPlanLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long planId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
