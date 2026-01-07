package com.planpolicy.client;

import com.planpolicy.request.UserDetailsRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthUserClientMockTest {

    @Test
    void mockFeignClientResponse_Works() {

        UserDetailsRequest user =
                new UserDetailsRequest(5L, "abc", "a@mail.com", "ROLE_CUSTOMER");

        assertEquals(5L, user.id());
        assertEquals("ROLE_CUSTOMER", user.role());
    }
}
