package com.planpolicy.controller;

import com.planpolicy.entity.Policy;
import com.planpolicy.jwt.JwtUtil;
import com.planpolicy.repository.PolicyRepository;
import com.planpolicy.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PolicyController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class PolicyControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private PolicyService service;
    @MockBean private PolicyRepository repo;
    @MockBean private JwtUtil jwtUtil;

    @Test
    void renewPolicy_Success() throws Exception {
        mvc.perform(put("/api/policy/users/renew/5/10")).andExpect(status().isOk());
    }

    @Test
    void getPaymentAmount_Success() throws Exception {
        com.planpolicy.entity.Policy policy = new com.planpolicy.entity.Policy();
        policy.setPremiumPaid(1200.0);
        when(service.getPolicy(1L)).thenReturn(policy);

        mvc.perform(get("/api/policy/payment/amount/1"))
           .andExpect(status().isOk())
           .andExpect(content().string("1200.0"));
    }

    @Test
    void confirmPayment_Success() throws Exception {
        mvc.perform(put("/api/policy/payment/confirm/1")).andExpect(status().isOk());
    }

    @Test
    void getUserPolicy_OwnershipCheck_Pass() throws Exception {
        com.planpolicy.entity.Policy policy = new com.planpolicy.entity.Policy();
        policy.setUserId(5L);
        when(service.getPolicy(10L)).thenReturn(policy);

        mvc.perform(get("/api/policy/users/5/10")).andExpect(status().isOk());
    }

    @Test
    void getUserPolicy_OwnershipCheck_Fail_ThrowsAccessDenied() {
        // Arrange
        Long userId = 5L;
        Long policyId = 10L;
        com.planpolicy.entity.Policy policy = new com.planpolicy.entity.Policy();
        policy.setUserId(99L); // Set different ID to trigger the 'if' branch
        
        when(service.getPolicy(policyId)).thenReturn(policy);

        // Act & Assert
        // This catches the org.springframework.security.access.AccessDeniedException
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mvc.perform(get("/api/policy/users/" + userId + "/" + policyId));
        });

        org.junit.jupiter.api.Assertions.assertTrue(
            exception.getCause() instanceof org.springframework.security.access.AccessDeniedException
        );
        org.junit.jupiter.api.Assertions.assertTrue(
            exception.getCause().getMessage().contains("Policy does not belong to user " + userId)
        );
    }
    
    @Test
    void controller_remainingEndpoints_Coverage() throws Exception {
        // Corrected: Added /api prefix to all paths
    	mvc.perform(delete("/api/admin/policy/1")).andExpect(status().isOk());
        mvc.perform(get("/api/admin/policy/all")).andExpect(status().isOk());
        mvc.perform(get("/api/admin/policy/details/1")).andExpect(status().isOk());

        // 2. Target User/Status Endpoints
        mvc.perform(put("/api/policy/users/status/5/1")).andExpect(status().isOk());
        mvc.perform(put("/api/policy/users/status/activate/5/1")).andExpect(status().isOk());
        
        // 3. Target Inventory and Enrollment
        mvc.perform(get("/api/policy/inventory/users/5")).andExpect(status().isOk());
        mvc.perform(post("/api/policy/enroll/5/1")).andExpect(status().isOk());
        
        // 4. Target public/user details endpoint
        mvc.perform(get("/api/user/policy/details/1")).andExpect(status().isOk());
    }
    
   
}