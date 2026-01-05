package com.authentication.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.authentication.entity.BlacklistedToken;
import com.authentication.entity.RefreshToken;
import com.authentication.entity.Role;
import com.authentication.entity.User;
import com.authentication.repository.AuthRepository;
import com.authentication.repository.BlacklistedTokenRepository;
import com.authentication.repository.RefreshTokenRepository;
import com.authentication.request.ChangePasswordRequest;
import com.authentication.request.JWTResponse;
import com.authentication.request.LoginRequest;
import com.authentication.request.RefreshRequest;
import com.authentication.request.SigninRequest;
import com.authentication.request.SigninResponse;
import com.authentication.security.JWTUtils;
import com.authentication.service.PasswordChangeService;
import com.authentication.service.PasswordPolicyService;

import jakarta.transaction.Transactional;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthRepository users;
    private final BlacklistedTokenRepository blacklist;
    private final RefreshTokenRepository refreshRepo;
    private final PasswordEncoder encoder;
    private final PasswordPolicyService policy;
    private final PasswordChangeService changeService;
    private final JWTUtils jwt;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SigninRequest req) {

        if (users.existsByEmail(req.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");

        User u = users.save(
                User.builder()
                        .username(req.getUsername())
                        .email(req.getEmail())
                        .password(encoder.encode(req.getPassword()))
                        .role(Role.ROLE_CUSTOMER)
                        .organizationId(null)
                        .active(true)
                        .passwordHistory(new ArrayList<>())
                        .lastPasswordChangedAt(new Date())
                        .failedAttempts(0)
                        .build());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SigninResponse(
                        u.getId(), u.getUsername(), u.getEmail(), u.getRole()
                ));
    }
    
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody SigninRequest req) {

        User u = users.save(
                User.builder()
                        .username(req.getUsername())
                        .email(req.getEmail())
                        .password(encoder.encode(req.getPassword()))
                        .role(Role.ROLE_ADMIN)
                        .active(true)
                        .build()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
        		.body(new SigninResponse(
                        u.getId(), u.getUsername(), u.getEmail(), u.getRole()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

    	User user = users.findByEmail(req.getEmail())
    	        .orElseThrow(() -> new ResponseStatusException(
    	                HttpStatus.UNAUTHORIZED,
    	                "Invalid credentials"
    	        ));

    	user.setFailedAttempts(
    	        user.getFailedAttempts() == null ? 0 : user.getFailedAttempts()
    	);

    	user.setPasswordHistory(
    	        user.getPasswordHistory() == null ? new ArrayList<>() : user.getPasswordHistory()
    	);

    	user.setLastPasswordChangedAt(
    	        user.getLastPasswordChangedAt() == null ? new Date() : user.getLastPasswordChangedAt()
    	);


        if (policy.isLocked(user))
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("Account locked. Try later");

        if (!encoder.matches(req.getPassword(), user.getPassword())) {

            policy.applyFailedAttempt(user);
            users.save(user);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        policy.clearLock(user);

        if (policy.isExpired(user) || user.isForcePasswordChange())
            return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED)
                    .body("Password expired. Change password");

        users.save(user);

        String access = jwt.generateAccessToken(
                user.getId(), user.getEmail(), user.getUsername(),
                user.getRole(), user.getOrganizationId());

        String refresh = jwt.generateRefreshToken(user.getId(), user.getEmail());

        refreshRepo.deleteAllByUserId(user.getId());
        refreshRepo.save(
                RefreshToken.builder()
                        .userId(user.getId())
                        .token(refresh)
                        .expiryDate(jwt.getClaims(refresh).getExpiration())
                        .revoked(false)
                        .build());

        return ResponseEntity.ok(
                new JWTResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getOrganizationId(),
                        access,
                        refresh
                )
        );
    }
    
    @Transactional
    @PostMapping("/signout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String header) {

        if (!header.startsWith("Bearer "))
            return ResponseEntity.badRequest().body("Invalid header");

        String token = header.substring(7);

        if (!jwt.validate(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid token");

        if (blacklist.existsByToken(token))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already signed out");

        blacklist.save(
                BlacklistedToken.builder()
                        .token(token)
                        .expiryDate(jwt.getClaims(token).getExpiration())
                        .build());

        Long userId = ((Number) jwt.getClaims(token).get("userId")).longValue();
        refreshRepo.deleteAllByUserId(userId);

        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Logged out successfully"
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {

        RefreshToken ref = refreshRepo.findByToken(req.getRefreshToken())
        		.orElseThrow(() -> new ResponseStatusException(
        		        HttpStatus.UNAUTHORIZED,
        		        "Invalid refresh token"
        		));


        if (ref.isRevoked() || ref.getExpiryDate().before(new Date()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh expired");

        User user = users.findById(ref.getUserId())
                .orElseThrow();

        String access = jwt.generateAccessToken(
                user.getId(), user.getEmail(), user.getUsername(),
                user.getRole(), user.getOrganizationId());

        return ResponseEntity.ok(Map.of("accessToken", access));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String header,
            @RequestBody ChangePasswordRequest req) {

        String token = header.substring(7);
        String email = jwt.getClaims(token).getSubject();

        User user = users.findByEmail(email).orElseThrow();

        changeService.changePassword(user, req);

        return ResponseEntity.ok("Password updated");
    }
    @GetMapping("/users/{userId}")
    public SigninResponse getUserById(@PathVariable Long userId) {

        User user = users.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new SigninResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
    
    
    @GetMapping("/user/userid/{email}")
    public ResponseEntity<?> getUserIdByEmail(@PathVariable String email) {

        User user = users.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "User not found with email: " + email
                        )
                );

        return ResponseEntity.ok(
                Map.of(
                        "userId", user.getId(),
                        "email", user.getEmail(),
                        "username", user.getUsername(),
                        "role", user.getRole()
                )
        );
    }


}
