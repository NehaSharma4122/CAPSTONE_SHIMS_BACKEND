package com.hospitalmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospitalmanager.entity.HospitalPlanLink;

public interface HospitalPlanRepository extends JpaRepository<HospitalPlanLink, Long> {
    List<HospitalPlanLink> findByPlanId(Long planId);
    boolean existsByHospitalIdAndPlanId(Long hospitalId, Long planId);

}
