package com.authentication.request;

import com.authentication.entity.Role;

import lombok.Data;

@Data
public class AdminCreateUserRequest {
	private String username;
    private String email;
    private String password;
    private Role role;     
    private String organizationId; 
}
