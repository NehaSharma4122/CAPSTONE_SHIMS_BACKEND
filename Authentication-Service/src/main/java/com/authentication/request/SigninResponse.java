package com.authentication.request;

import com.authentication.entity.Role;

import lombok.*;

@Data
@AllArgsConstructor
public class SigninResponse {

    private Long userId;
    private String username;
    private String email;
    private Role role;
}
