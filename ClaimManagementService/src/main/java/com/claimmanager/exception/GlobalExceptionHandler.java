package com.claimmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildResponse(String message, HttpStatus status) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.name());
        body.put("message", message);

        return body;
    }

    // ============================
    // Custom Exceptions
    // ============================

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<?> handleClaimNotFound(ClaimNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorized(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(InvalidClaimStatusException.class)
    public ResponseEntity<?> handleInvalidStatus(InvalidClaimStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }
    
    @ExceptionHandler(PolicyValidationException.class)
    public ResponseEntity<?> handlePolicyValidation(PolicyValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(feign.FeignException.Forbidden.class)
    public ResponseEntity<?> handleFeignForbidden(feign.FeignException.Forbidden ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(
                        "Policy does not belong to user",
                        HttpStatus.BAD_REQUEST
                ));
    }

    // ============================
    // Validation Errors
    // ============================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = buildResponse(
                "Validation failed",
                HttpStatus.BAD_REQUEST
        );

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        errors.put("fields", fieldErrors);

        return ResponseEntity.badRequest().body(errors);
    }

    // ============================
    // Generic Exception Catcher
    // ============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobal(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
