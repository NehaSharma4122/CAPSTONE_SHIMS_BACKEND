package com.planpolicy.controller;

import com.planpolicy.TestSecurityConfig;
import com.planpolicy.entity.Policy;
import com.planpolicy.repository.PolicyRepository;
import com.planpolicy.service.PolicyService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@WebMvcTest(controllers = PolicyController.class)
@AutoConfigureMockMvc(addFilters = false) // 1. Disable security filters
@Import(TestSecurityConfig.class)
class PolicyControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PolicyService service;

    @MockBean
    PolicyRepository repo;

    @Test
    void fetchUserPolicies_Authorized() throws Exception {

        when(repo.findByUserId(5L))
                .thenReturn(List.of(new Policy()));

        mvc.perform(get("/api/policy/inventory/users/5"))
                .andExpect(status().isOk());
    }

    @Test
    void fetchUserPolicies_Forbidden_MockedByServiceError() throws Exception {

        when(repo.findByUserId(5L))
                .thenThrow(new RuntimeException("Unauthorized access"));

        mvc.perform(get("/api/policy/inventory/users/5"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void enrollPolicy_Allowed() throws Exception {

        when(service.enrollPolicy(5L, 1L))
                .thenReturn(new Policy());

        mvc.perform(post("/api/policy/enroll/5/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deletePolicy_DeniedForCustomer_MockedAtServiceLayer() throws Exception {

        doThrow(new RuntimeException("Only Admin can delete policies"))
                .when(service).deletePolicy(9L);

        mvc.perform(delete("/api/admin/policy/9"))
                .andExpect(status().is5xxServerError());
    }
}
