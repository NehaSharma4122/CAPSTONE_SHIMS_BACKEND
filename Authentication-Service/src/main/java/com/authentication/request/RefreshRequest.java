package com.authentication.request;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;
}