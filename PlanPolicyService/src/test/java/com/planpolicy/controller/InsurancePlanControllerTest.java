package com.planpolicy.controller;

import com.planpolicy.entity.InsurancePlan;
import com.planpolicy.jwt.JwtUtil;
import com.planpolicy.repository.InsurancePlanRepository;
import com.planpolicy.service.InsurancePlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InsurancePlanController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureMockMvc(addFilters = false)
class InsurancePlanControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private InsurancePlanService service;
    @MockBean private InsurancePlanRepository repo;
    @MockBean private JwtUtil jwtUtil;

    @Test
    void getPlanById_Found() throws Exception {
        InsurancePlan plan = new InsurancePlan();
        plan.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(plan));

        mvc.perform(get("/api/plans/1")).andExpect(status().isOk());
    }

    @Test
    void getPlanById_NotFound_ThrowsException() {
        // Arrange
        Long planId = 3L;
        when(repo.findById(planId)).thenReturn(Optional.empty());

        // Act & Assert
        // This catches the exception thrown by: .orElseThrow(() -> new RuntimeException(...))
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            mvc.perform(get("/api/plans/" + planId));
        });

        org.junit.jupiter.api.Assertions.assertTrue(
            exception.getCause().getMessage().contains("Plan not found with id " + planId)
        );
    }

    @Test
    void doesPlanExist_ReturnsBoolean() throws Exception {
        when(service.exists(1L)).thenReturn(true);
        mvc.perform(get("/api/plans/exists/1"))
           .andExpect(status().isOk())
           .andExpect(content().string("true"));
    }

    @Test
    void updatePlan_Success() throws Exception {
        InsurancePlan existing = new InsurancePlan();
        existing.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenReturn(existing);

        mvc.perform(put("/api/admin/plans/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"planName\":\"Updated\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void cancelPlan_Success() throws Exception {
        when(repo.existsById(1L)).thenReturn(true);
        mvc.perform(delete("/api/admin/plans/cancel/1")).andExpect(status().isOk());
    }

    @Test
    void cancelPlan_NotFound() throws Exception {
        when(repo.existsById(1L)).thenReturn(false);
        mvc.perform(delete("/api/admin/plans/cancel/1")).andExpect(status().isNotFound());
    }
}