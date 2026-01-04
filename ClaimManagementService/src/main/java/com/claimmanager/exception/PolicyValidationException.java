package com.claimmanager.exception;

public class PolicyValidationException extends RuntimeException {

    public PolicyValidationException(String message) {
        super(message);
    }
}