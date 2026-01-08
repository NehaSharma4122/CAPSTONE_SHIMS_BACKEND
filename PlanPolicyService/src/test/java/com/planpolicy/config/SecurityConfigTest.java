package com.planpolicy.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    // This satisfies the OAuth2 dependency so the context can start
    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void securityConfig_Loads() {
        assertNotNull(context.getBean(SecurityConfig.class));
    }
}