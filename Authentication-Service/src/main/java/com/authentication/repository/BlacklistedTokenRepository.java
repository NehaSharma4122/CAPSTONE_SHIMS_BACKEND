package com.authentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authentication.entity.BlacklistedToken;

public interface BlacklistedTokenRepository
extends JpaRepository<BlacklistedToken, Long> {

boolean existsByToken(String token);
}

