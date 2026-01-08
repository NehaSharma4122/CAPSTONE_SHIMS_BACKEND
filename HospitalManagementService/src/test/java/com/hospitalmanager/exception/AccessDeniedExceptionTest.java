package com.hospitalmanager.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccessDeniedExceptionTest {

    @Test
    void shouldStoreMessage() {
        AccessDeniedException ex =
                new AccessDeniedException("Access blocked");

        assertEquals("Access blocked", ex.getMessage());
    }
}
