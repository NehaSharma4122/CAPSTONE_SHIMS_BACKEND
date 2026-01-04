package com.claimmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.claimmanager.entity.Claim;
import com.claimmanager.entity.ClaimStatus;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByUserId(Long userId);

    List<Claim> findByStatus(ClaimStatus status);
    
    List<Claim> findByHospitalId(Long hospitalId);

}