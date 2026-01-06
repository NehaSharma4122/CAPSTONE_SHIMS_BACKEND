package com.paymentgateway.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor authForwardingInterceptor() {

        return requestTemplate -> {

            var context = SecurityContextHolder.getContext();
            var auth = context.getAuthentication();

            if (auth == null)
                return;

            Object credentials = auth.getCredentials();

            if (credentials != null) {
                requestTemplate.header(
                        "Authorization",
                        "Bearer " + credentials.toString()
                );
            }
        };
    }
}
