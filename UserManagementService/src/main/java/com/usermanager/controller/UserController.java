package com.usermanager.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.usermanager.client.AuthFeignClient;
import com.usermanager.request.*;
import com.usermanager.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {

    private final AuthFeignClient authClient;
    private final EmailService emailService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public List<UserResponse> getUsers() {
        return authClient.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/agent/add")
    public UserResponse addAgent(@RequestBody CreateUserRequest req) {
        req.setRole(Role.ROLE_AGENT);
        UserResponse created = authClient.createUser(req);
        emailService.sendUserCreationEmail(req, created);
        return authClient.createUser(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/claimoff/add")
    public UserResponse addClaimsOfficer(@RequestBody CreateUserRequest req) {
        req.setRole(Role.ROLE_CLAIMS_OFFICER);
        UserResponse created = authClient.createUser(req);
        emailService.sendUserCreationEmail(req, created);
        return authClient.createUser(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/hospital/add")
    public UserResponse addHospital(@RequestBody CreateUserRequest req) {
        req.setRole(Role.ROLE_HOSPITAL);
        UserResponse created = authClient.createUser(req);
        emailService.sendUserCreationEmail(req, created);
        return authClient.createUser(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/cancel/{id}")
    public ActionResponse deleteUser(@PathVariable Long id) {

        authClient.deleteUser(id);

        return new ActionResponse(
                "SUCCESS",
                "User deleted successfully with id = " + id
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/role/{id}")
    public UserResponse updateRole(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest req) {
        UserResponse updatedUser = authClient.updateRole(id, req.getRole());

        emailService.sendRoleUpdateEmail(updatedUser, req.getRole());
        return authClient.updateRole(id, req.getRole());
    }
}
