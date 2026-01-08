package com.hospitalmanager.feign;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeignAuthInterceptorTest {

    FeignAuthInterceptor interceptor = new FeignAuthInterceptor();

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addsAuthorizationHeader_whenTokenPresent() {

        var auth = new UsernamePasswordAuthenticationToken(
                "10",
                "abc-token",
                List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().containsKey("Authorization"));
        assertEquals(
                "Bearer abc-token",
                template.headers().get("Authorization").iterator().next()
        );
    }

    @Test
    void doesNothing_whenNoAuthPresent() {

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().isEmpty());
    }
}
