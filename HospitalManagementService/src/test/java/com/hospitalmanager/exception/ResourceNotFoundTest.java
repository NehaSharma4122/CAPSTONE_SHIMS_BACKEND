package com.hospitalmanager.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundTest {

    @Test
    void shouldStoreMessage() {
        ResourceNotFound ex =
                new ResourceNotFound("Not found");

        assertEquals("Not found", ex.getMessage());
    }
}
