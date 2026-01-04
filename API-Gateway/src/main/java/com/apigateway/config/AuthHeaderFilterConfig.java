package com.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class AuthHeaderFilterConfig {

    @Bean
    public GlobalFilter authHeaderFilter() {

        return (exchange, chain) -> {

            HttpHeaders headers = exchange.getRequest().getHeaders();

            String authHeader = headers.getFirst("Authorization");

            if (authHeader != null && !authHeader.isBlank()) {
                return chain.filter(exchange);
            }

            return chain.filter(exchange);
        };
    }
}
