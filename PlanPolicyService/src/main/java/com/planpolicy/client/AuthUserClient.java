package com.planpolicy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.planpolicy.request.UserDetailsRequest;

@FeignClient(name = "Authentication-Service")
public interface AuthUserClient {

    @GetMapping("/api/auth/users/{userId}")
    UserDetailsRequest getUserById(@PathVariable Long userId);
}
