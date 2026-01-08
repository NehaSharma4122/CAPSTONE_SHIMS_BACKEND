package com.claimmanager.config;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class FeignClientConfigTests {

    @Test
    void testInterceptorAddsBearerToken() {

        FeignClientConfig config = new FeignClientConfig();

        var interceptor = config.authForwardingInterceptor();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "14",
                        "abc.jwt.token",
                        null
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertThat(template.headers().get("Authorization"))
                .containsExactly("Bearer abc.jwt.token");
    }

    @Test
    void testInterceptorSkipsWhenNoCredentials() {

        SecurityContextHolder.clearContext();

        FeignClientConfig config = new FeignClientConfig();

        var interceptor = config.authForwardingInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertThat(template.headers()).doesNotContainKey("Authorization");
    }
}
