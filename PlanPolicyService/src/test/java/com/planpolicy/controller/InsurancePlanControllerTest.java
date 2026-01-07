package com.planpolicy.controller;

import com.planpolicy.TestSecurityConfig;
import com.planpolicy.entity.InsurancePlan;
import com.planpolicy.repository.InsurancePlanRepository;
import com.planpolicy.service.InsurancePlanService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@WebMvcTest(controllers = InsurancePlanController.class)
@AutoConfigureMockMvc(addFilters = false) // 1. Disable security filters
@Import(TestSecurityConfig.class)
class InsurancePlanControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    InsurancePlanService service;

    @MockBean
    InsurancePlanRepository repo;

    @Test
    void getPlans_ReturnsOk() throws Exception {

        when(repo.findAll()).thenReturn(List.of(new InsurancePlan()));

        mvc.perform(get("/api/plans/inventory"))
                .andExpect(status().isOk());
    }

    @Test
    void addPlan_AllowedForAdmin() throws Exception {

        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName("Gold");

        when(service.addPlan(any())).thenReturn(plan);

        mvc.perform(post("/api/admin/plans/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"planName":"Gold","premiumAmount":100,"coverageLimitType":"PERCENT","coverageLimitValue":200}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void addPlan_DeniedForCustomer_MockedByServiceException() throws Exception {

        when(service.addPlan(any()))
                .thenThrow(new RuntimeException("Only Admin can manage insurance plans"));

        mvc.perform(post("/api/admin/plans/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is5xxServerError());
    }
}
