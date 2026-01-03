package com.usermanager.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
    private String organizationId;
}
