package com.usermanager.config;

import com.usermanager.jwt.JWTAuthFilter;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    SecurityFilterChain chain;

    @Autowired
    JWTAuthFilter filter;

    @Test
    void securityBeansShouldLoad() {
        assertThat(chain).isNotNull();
        assertThat(filter).isNotNull();
    }
}
