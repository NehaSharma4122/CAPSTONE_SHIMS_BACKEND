package com.authentication.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.authentication.entity.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JWTUtils {

    private final SecretKey key;
    private final long accessExpiry;
    private final long refreshExpiry;

    public JWTUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expiry}") long accessExpiry,
            @Value("${jwt.refresh.expiry}") long refreshExpiry) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiry = accessExpiry;
        this.refreshExpiry = refreshExpiry;
    }

    public String generateAccessToken(
            Long userId,
            String email,
            String username,
            Role role,
            String organizationId
    ) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessExpiry);

        return Jwts.builder()
                .setSubject(email)
                .addClaims(Map.of(
                        "userId", userId,
                        "username", username,
                        "role", role.name(),
                        "organizationId", organizationId == null ? "" : organizationId
                ))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId, String email) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshExpiry);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validate(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
