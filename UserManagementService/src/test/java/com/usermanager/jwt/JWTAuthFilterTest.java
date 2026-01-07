package com.usermanager.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JWTAuthFilterTest {

    private final JWTUtils jwtUtils = mock(JWTUtils.class);
    private final JWTAuthFilter filter = new JWTAuthFilter(jwtUtils);

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void filter_setsAuthenticationWhenTokenPresent() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization"))
                .thenReturn("Bearer test-token");

        when(jwtUtils.extractUserId("test-token"))
                .thenReturn("123");

        when(jwtUtils.extractRole("test-token"))
                .thenReturn("ROLE_ADMIN");

        filter.doFilterInternal(req, res, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.getPrincipal()).isEqualTo("123");
        assertThat(auth.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_ADMIN");

        verify(chain).doFilter(req, res);
    }

    @Test
    void filter_doesNothingWhenNoTokenPresent() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(req, res, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();

        verify(chain).doFilter(req, res);
    }
}
