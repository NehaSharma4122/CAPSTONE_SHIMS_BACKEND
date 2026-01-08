package com.claimmanager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.claimmanager.jwt.JWTUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTests {

    @Autowired
    MockMvc mvc;
    
    @MockBean
    private JWTUtils jwtUtils;

    @Test
    void testClaimsEndpointRequiresAuth() throws Exception {
        mvc.perform(get("/api/claims/all").with(csrf())) // Add csrf()
                .andExpect(status().isForbidden()); 
    }
    
    
}
