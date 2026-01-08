package com.claimmanager.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JWTUtilsTests {

    private JWTUtils jwtUtils;
    private SecretKey key;
    private static final String SECRET = "ThisIsAStrongJwtSecretKeyWithAtLeast64CharactersLengthForHS512Algorithm";

    @BeforeEach
    void setUp() {
        jwtUtils = new JWTUtils();
        key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testExtractUserId_WithCustomUserIdClaim() {
        // Prepare token with "userId" claim
        String token = Jwts.builder()
                .claim("userId", 123)
                .signWith(key)
                .compact();

        String result = jwtUtils.extractUserId(token);
        
        assertThat(result).isEqualTo("123");
    }

    @Test
    void testExtractUserId_WithSubjectOnly() {
        // Prepare token where "userId" is null, should fallback to Subject
        String token = Jwts.builder()
                .setSubject("testUser")
                .signWith(key)
                .compact();

        String result = jwtUtils.extractUserId(token);
        
        assertThat(result).isEqualTo("testUser");
    }

    @Test
    void testExtractRole() {
        // Prepare token with "role" claim
        String token = Jwts.builder()
                .claim("role", "ROLE_ADMIN")
                .signWith(key)
                .compact();

        String result = jwtUtils.extractRole(token);
        
        assertThat(result).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testGetSigningKeyAndGetClaims_InternalExecution() {
        // This test ensures the private getClaims and getSigningKey are fully exercised
        String token = Jwts.builder()
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(key)
                .compact();

        // Calling any public method triggers the private internal methods
        assertThat(jwtUtils.extractUserId(token)).isEqualTo("test");
    }
}