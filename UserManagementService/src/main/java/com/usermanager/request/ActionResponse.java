package com.usermanager.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResponse {
    private String status;
    private String message;
}
