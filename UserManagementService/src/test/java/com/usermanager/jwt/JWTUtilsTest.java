package com.usermanager.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JWTUtilsTest {

    private final JWTUtils jwtUtils = new JWTUtils();

    private String generateToken(Map<String, Object> claims) {

        var key = Keys.hmacShaKeyFor(
                "ThisIsAStrongJwtSecretKeyWithAtLeast64CharactersLengthForHS512Algorithm"
                        .getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.builder()
                .claims(claims)
                .subject("fallback-user")
                .signWith(key)
                .compact();
    }

    @Test
    void extractUserId_returnsUserIdClaimWhenPresent() {
        String token = generateToken(Map.of(
                "userId", 99,
                "role", "ROLE_ADMIN"
        ));

        assertThat(jwtUtils.extractUserId(token)).isEqualTo("99");
    }

    @Test
    void extractUserId_returnsSubjectWhenUserIdMissing() {
        String token = generateToken(Map.of(
                "role", "ROLE_AGENT"
        ));

        assertThat(jwtUtils.extractUserId(token)).isEqualTo("fallback-user");
    }

    @Test
    void extractRole_returnsRoleClaim() {
        String token = generateToken(Map.of(
                "role", "ROLE_HOSPITAL"
        ));

        assertThat(jwtUtils.extractRole(token)).isEqualTo("ROLE_HOSPITAL");
    }
}
