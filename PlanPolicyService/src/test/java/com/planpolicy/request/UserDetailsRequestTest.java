package com.planpolicy.request;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDetailsRequestTest {

    @Test
    void recordStoresValuesCorrectly() {

        UserDetailsRequest user = new UserDetailsRequest(
                10L,
                "neha",
                "neha@mail.com",
                "ROLE_CUSTOMER"
        );

        assertEquals(10L, user.id());
        assertEquals("neha", user.username());
        assertEquals("neha@mail.com", user.email());
        assertEquals("ROLE_CUSTOMER", user.role());
    }
}
