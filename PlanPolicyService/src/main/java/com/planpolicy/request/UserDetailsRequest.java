package com.planpolicy.request;

public record UserDetailsRequest (
	Long id,
    String username,
    String email,
    String role
){}
