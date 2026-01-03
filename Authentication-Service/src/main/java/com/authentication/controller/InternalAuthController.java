package com.authentication.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.authentication.entity.Role;
import com.authentication.entity.User;
import com.authentication.repository.AuthRepository;
import com.authentication.repository.RefreshTokenRepository;
import com.authentication.request.AdminCreateUserRequest;
import com.authentication.request.SigninResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/internal/auth")
@RequiredArgsConstructor
public class InternalAuthController {

    private final AuthRepository users;
    private final PasswordEncoder encoder;
    private final RefreshTokenRepository refreshRepo;

    @PostMapping("/create-user")
    public SigninResponse createUser(@RequestBody AdminCreateUserRequest req) {

        if (users.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already exists");

        User u = users.save(
            User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .organizationId(req.getOrganizationId())
                .active(true)
                .passwordHistory(new ArrayList<>())
                .lastPasswordChangedAt(new Date())
                .failedAttempts(0)
                .build()
        );

        return new SigninResponse(
            u.getId(), u.getUsername(), u.getEmail(), u.getRole()
        );
    }

    @PutMapping("/users/{id}/role")
    public SigninResponse updateRole(
            @PathVariable Long id,
            @RequestParam Role role) {

        User user = users.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        users.save(user);

        return new SigninResponse(
            user.getId(), user.getUsername(), user.getEmail(), user.getRole()
        );
    }

    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        User user = users.findById(id)
                .orElseThrow(() -> 
                	new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        refreshRepo.deleteAllByUserId(id);
        users.delete(user); 
        return Map.of(
            "status", "SUCCESS",
            "message", "User deleted permanently",
            "userId", id
        );
    }


    @GetMapping("/users")
    public List<SigninResponse> getAllUsers() {

        return users.findAll().stream()
                .map(u -> new SigninResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole()
                ))
                .toList();
    }
}
