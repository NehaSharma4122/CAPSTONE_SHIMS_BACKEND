package com.planpolicy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.planpolicy.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByUserId(Long userId);
}
