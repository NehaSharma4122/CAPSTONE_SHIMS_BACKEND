package com.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.authentication.entity.RefreshToken;

import jakarta.transaction.Transactional;

public interface RefreshTokenRepository
extends JpaRepository<RefreshToken, Long> {

Optional<RefreshToken> findByToken(String token);

@Modifying
@Transactional
void deleteAllByUserId(Long userId);

}

