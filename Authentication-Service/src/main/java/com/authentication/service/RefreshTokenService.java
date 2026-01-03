package com.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.authentication.entity.RefreshToken;
import com.authentication.repository.RefreshTokenRepository;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    public RefreshToken create(Long userId, long expiryMs) {

        repo.deleteAllByUserId(userId);

        return repo.save(
                RefreshToken.builder()
                        .userId(userId)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(new Date(System.currentTimeMillis() + expiryMs))
                        .revoked(false)
                        .build());
    }
}
