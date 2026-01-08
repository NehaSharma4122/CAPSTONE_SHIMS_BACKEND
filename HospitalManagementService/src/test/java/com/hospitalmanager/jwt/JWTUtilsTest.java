package com.hospitalmanager.jwt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Map;

class JWTUtilsTest {

    JWTUtils utils = new JWTUtils();

    private static final String SECRET =
        "ThisIsAStrongJwtSecretKeyWithAtLeast64CharactersLengthForHS512Algorithm";

    @Test
    void extractsUserIdAndRole_fromClaims() {

        var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        String token =
                Jwts.builder()
                        .subject("backupSubject")
                        .claims(Map.of(
                                "userId", 99,
                                "role", "ROLE_HOSPITAL"
                        ))
                        .signWith(key)
                        .compact();

        assertEquals("99", utils.extractUserId(token));
        assertEquals("ROLE_HOSPITAL", utils.extractRole(token));
    }

    @Test
    void fallsBackToSubject_whenUserIdMissing() {

        var key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        String token =
                Jwts.builder()
                        .subject("33")
                        .claims(Map.of(
                                "role", "ROLE_ADMIN"
                        ))
                        .signWith(key)
                        .compact();

        assertEquals("33", utils.extractUserId(token));
    }
}
