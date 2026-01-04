package com.hospitalmanager.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null)
            return;

        String token = (String) auth.getCredentials();

        if (token != null) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
