package com.hospitalmanager.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesNotFound() {

        ResourceNotFound ex = new ResourceNotFound("Missing");

        ResponseEntity<?> res = handler.notFound(ex);

        assertEquals(404, res.getStatusCode().value());
        assertEquals("Missing", res.getBody());
    }

    @Test
    void handlesAccessDenied() {

        AccessDeniedException ex =
                new AccessDeniedException("Forbidden");

        ResponseEntity<?> res = handler.handleAccessDenied(ex);

        assertEquals(403, res.getStatusCode().value());
        assertEquals("Forbidden", res.getBody());
    }
}
