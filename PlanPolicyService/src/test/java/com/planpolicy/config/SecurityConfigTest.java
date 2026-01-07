package com.planpolicy.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    private final ApplicationContext context;

    SecurityConfigTest(ApplicationContext context) {
        this.context = context;
    }

    @Test
    void securityConfig_BuildsFilterChainSuccessfully() {
        assertNotNull(context.getBean(SecurityConfig.class));
        assertNotNull(context.getBean("filterChain"));
    }
}
