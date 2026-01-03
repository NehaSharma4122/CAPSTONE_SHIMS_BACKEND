package com.authentication.request;

import com.authentication.entity.Role;

import lombok.*;

@Data
@AllArgsConstructor
public class JWTResponse {

    private Long userId;
    private String username;
    private String email;
    private Role role;
    private String organizationId;
    private String accessToken;
    private String refreshToken;}
