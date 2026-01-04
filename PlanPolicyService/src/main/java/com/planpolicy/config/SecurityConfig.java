package com.planpolicy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import com.planpolicy.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/plans/**").permitAll()

            .requestMatchers("/api/admin/plans/**")
                .hasRole("ADMIN")

            .requestMatchers("/api/admin/policy/**")
                .hasRole("ADMIN")

            .requestMatchers("/api/policy/enroll/**").hasAnyRole("AGENT","CUSTOMER")
            .requestMatchers("/api/policy/users/renew/**").hasAnyRole("AGENT","CUSTOMER")
            .requestMatchers("/api/policy/users/status/**").hasAnyRole("AGENT","ADMIN","CUSTOMER")


            .requestMatchers("/api/policy/inventory/users/**")
                .hasAnyRole("CUSTOMER","AGENT","ADMIN")
            .requestMatchers("/api/policy/users/**")
                .hasAnyRole("CUSTOMER","AGENT","ADMIN")
            .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }
}

