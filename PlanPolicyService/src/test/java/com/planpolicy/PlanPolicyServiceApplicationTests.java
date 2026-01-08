package com.planpolicy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
class PlanPolicyServiceApplicationTests {

    @MockBean
    private JwtDecoder jwtDecoder;
    
    @Test
    void main() {
        // This covers the static main method branch
        PlanPolicyServiceApplication.main(new String[] {});
    }
    
//    @Test
//    void contextLoads() {
//        // This will now pass because JwtDecoder is mocked
//    }
   
}