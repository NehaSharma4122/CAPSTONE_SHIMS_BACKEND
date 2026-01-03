package com.usermanager.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.usermanager.request.ActionResponse;
import com.usermanager.request.CreateUserRequest;
import com.usermanager.request.UserResponse;
import com.usermanager.request.Role;

@FeignClient(name = "Authentication-Service")
public interface AuthFeignClient {

    @PostMapping("/api/internal/auth/create-user")
    UserResponse createUser(@RequestBody CreateUserRequest req);

    @PutMapping("/api/internal/auth/users/{id}/role")
    UserResponse updateRole(
            @PathVariable Long id,
            @RequestParam Role role);
    
    @DeleteMapping("/api/internal/auth/users/{id}")
    ActionResponse deleteUser(@PathVariable Long id);
    
    @GetMapping("/api/internal/auth/users")
    List<UserResponse> getAllUsers();

}
