package com.hospitalmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.hospitalmanager.jwt.JWTAuthFilter;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final HospitalProfileGuard profileGuard;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/hospitals/onboarding")
                .hasRole("HOSPITAL")

                .requestMatchers(HttpMethod.POST,
                        "/api/hospitals/*/plans/*/link")
                .hasAnyRole("ADMIN", "HOSPITAL")

                .requestMatchers(HttpMethod.DELETE,
                        "/api/hospitals/*/plans/*/unlink")
                .hasAnyRole("ADMIN", "HOSPITAL")

                .requestMatchers("/api/hospitals/**")
                .access((authentication, ctx) -> {

                    var authObj = authentication.get();

                    Long userId = Long.parseLong((String) authObj.getPrincipal());

                    String role = authObj.getAuthorities()
                            .stream()
                            .findFirst()
                            .map(a -> a.getAuthority())
                            .orElse("");

                    profileGuard.verifyProfile(userId, role);

                    return new AuthorizationDecision(true);
                })

                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }
}
