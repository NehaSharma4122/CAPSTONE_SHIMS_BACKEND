package com.planpolicy.jwt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    private static final String SECRET =
        "ThisIsAStrongJwtSecretKeyWithAtLeast64CharactersLengthForHS512Algorithm";

    private String generateToken(Long userId, String role) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role)
                .subject("fallbackUser")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    @Test
    void extractUserId_ReturnsUserIdClaim() {

        String token = generateToken(21L, "ROLE_CUSTOMER");

        assertEquals("21", jwtUtil.extractUserId(token));
    }

    @Test
    void extractRole_ReturnsRoleClaim() {

        String token = generateToken(50L, "ROLE_ADMIN");

        assertEquals("ROLE_ADMIN", jwtUtil.extractRole(token));
    }

    @Test
    void extractUserId_FallsBackToSubject_WhenClaimMissing() {

        String token = Jwts.builder()
                .subject("fallbackUser")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();

        assertEquals("fallbackUser", jwtUtil.extractUserId(token));
    }
}
