package com.planpolicy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.planpolicy.entity.InsurancePlan;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Long> {}
