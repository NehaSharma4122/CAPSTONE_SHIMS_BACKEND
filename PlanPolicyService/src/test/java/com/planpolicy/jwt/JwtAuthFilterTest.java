package com.planpolicy.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthFilterTest {

    @Test
    void filter_SetsAuthentication_WhenTokenIsValid() throws Exception {

        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtAuthFilter filter = new JwtAuthFilter(jwtUtil);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer testtoken");

        when(jwtUtil.extractUserId("testtoken")).thenReturn("21");
        when(jwtUtil.extractRole("testtoken")).thenReturn("ROLE_CUSTOMER");

        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);
        assertNotNull(
            org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
        );
    }

    @Test
    void filter_SkipsAuth_WhenNoTokenPresent() throws Exception {

        JwtUtil jwtUtil = mock(JwtUtil.class);
        JwtAuthFilter filter = new JwtAuthFilter(jwtUtil);

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);
    }
}
