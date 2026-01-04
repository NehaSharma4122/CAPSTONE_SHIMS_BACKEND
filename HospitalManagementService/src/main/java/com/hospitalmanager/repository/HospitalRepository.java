package com.hospitalmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospitalmanager.entity.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
   
}
