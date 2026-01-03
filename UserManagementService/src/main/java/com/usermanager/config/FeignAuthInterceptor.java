package com.usermanager.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        var attrs = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attrs == null) return;

        String token = attrs.getRequest().getHeader("Authorization");

        if (token != null && !token.isBlank()) {
            template.header("Authorization", token);
        }
    }
}

