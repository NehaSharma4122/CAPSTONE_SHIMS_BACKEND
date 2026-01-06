package com.claimmanager.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.claimmanager.request.UserResponseDTO;

@FeignClient(
        name = "Authentication-Service"
)
public interface AuthUserClient {

    @GetMapping("/api/auth/users/{userId}")
    UserResponseDTO getUserById(@PathVariable Long userId);
}
