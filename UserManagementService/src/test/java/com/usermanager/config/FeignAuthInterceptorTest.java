package com.usermanager.config;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;

class FeignAuthInterceptorTest {

    FeignAuthInterceptor interceptor = new FeignAuthInterceptor();

    @Test
    void shouldPropagateAuthorizationHeader() {

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer xyz");

        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(req));

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers().get("Authorization"))
                .containsExactly("Bearer xyz");
    }

    @Test
    void shouldDoNothingIfNoRequestContext() {

        RequestContextHolder.resetRequestAttributes();

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers()).isEmpty();
    }
}
