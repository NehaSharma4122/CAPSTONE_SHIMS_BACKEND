package com.hospitalmanager.jwt;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.mock.web.*;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JWTAuthFilterTest {

    @Mock
    JWTUtils utils;

    @Mock
    FilterChain chain;

    JWTAuthFilter filter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        filter = new JWTAuthFilter(utils);
    }

    @Test
    void skips_whenNoAuthHeader() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();

        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    void authenticates_whenValidTokenProvided() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer test-token");

        MockHttpServletResponse res = new MockHttpServletResponse();

        when(utils.extractUserId("test-token")).thenReturn("20");
        when(utils.extractRole("test-token")).thenReturn("ROLE_HOSPITAL");

        filter.doFilterInternal(req, res, chain);

        assertNotNull(
                org.springframework.security.core.context.SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        verify(chain).doFilter(req, res);
    }
}
