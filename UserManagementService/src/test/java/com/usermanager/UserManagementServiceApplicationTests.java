package com.usermanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.openfeign.enabled=false",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://disabled",
        "spring.mail.host=disabled"
    }
)
class UserManagementServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}

