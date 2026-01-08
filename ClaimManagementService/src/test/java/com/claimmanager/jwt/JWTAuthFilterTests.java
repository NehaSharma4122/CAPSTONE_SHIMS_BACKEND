package com.claimmanager.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JWTAuthFilterTests {
	
	@BeforeEach
	void clearSecurityContext() {
	    SecurityContextHolder.clearContext();
	}
	
    @Test
    void testFilterPopulatesSecurityContext() throws Exception {

        JWTUtils utils = mock(JWTUtils.class);

        when(utils.extractUserId("abc.jwt")).thenReturn("14");
        when(utils.extractRole("abc.jwt")).thenReturn("ROLE_CUSTOMER");

        JWTAuthFilter filter = new JWTAuthFilter(utils);

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer abc.jwt");

        filter.doFilterInternal(
                req,
                new MockHttpServletResponse(),
                new MockFilterChain()
        );

        var auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.getPrincipal()).isEqualTo("14");
        assertThat(auth.getAuthorities().iterator().next().getAuthority())
                .isEqualTo("ROLE_CUSTOMER");
        assertThat(auth.getCredentials()).isEqualTo("abc.jwt");
    }

    @Test
    void testFilterSkipsWhenNoHeader() throws Exception {

        JWTAuthFilter filter = new JWTAuthFilter(mock(JWTUtils.class));

        MockHttpServletRequest req = new MockHttpServletRequest();

        filter.doFilterInternal(
                req,
                new MockHttpServletResponse(),
                new MockFilterChain()
        );

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }
}
