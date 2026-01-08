package com.claimmanager.exception;

import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTests {

    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testClaimNotFoundHandler() {
        ClaimNotFoundException ex = new ClaimNotFoundException("Claim missing");
        ResponseEntity<?> res = handler.handleClaimNotFound(ex);
        Map<?, ?> body = (Map<?, ?>) res.getBody();
        assertThat(body.get("message")).isEqualTo("Claim missing");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testUnauthorizedHandler() {
        UnauthorizedAccessException ex = new UnauthorizedAccessException("Access Denied");
        ResponseEntity<?> res = handler.handleUnauthorized(ex);
        Map<?, ?> body = (Map<?, ?>) res.getBody();
        assertThat(body.get("message")).isEqualTo("Access Denied");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void testInvalidClaimStatusHandler() {
        InvalidClaimStatusException ex = new InvalidClaimStatusException("Invalid");
        var res = handler.handleInvalidStatus(ex);
        assertThat(((Map<?, ?>) res.getBody()).get("message")).isEqualTo("Invalid");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testPolicyValidationHandler() {
        PolicyValidationException ex = new PolicyValidationException("Policy error");
        var res = handler.handlePolicyValidation(ex);
        assertThat(((Map<?, ?>) res.getBody()).get("message")).isEqualTo("Policy error");
    }

    @Test
    void testFeignForbiddenHandler() {
        // Mocking FeignException.Forbidden which is required for coverage
        FeignException.Forbidden ex = mock(FeignException.Forbidden.class);
        var res = handler.handleFeignForbidden(ex);
        Map<?, ?> body = (Map<?, ?>) res.getBody();
        assertThat(body.get("message")).isEqualTo("Policy does not belong to user");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testGlobalExceptionHandler() {
        Exception ex = new RuntimeException("Generic Error");
        var res = handler.handleGlobal(ex);
        Map<?, ?> body = (Map<?, ?>) res.getBody();
        assertThat(body.get("message")).isEqualTo("Something went wrong");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testHandleValidationErrors() {
        // We need to mock MethodArgumentNotValidException to trigger the field errors loop
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "status", "must not be null");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<?> res = handler.handleValidationErrors(ex);
        Map<String, Object> body = (Map<String, Object>) res.getBody();
        Map<String, String> fieldErrors = (Map<String, String>) body.get("fields");

        assertThat(body.get("message")).isEqualTo("Validation failed");
        assertThat(fieldErrors).containsEntry("status", "must not be null");
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}