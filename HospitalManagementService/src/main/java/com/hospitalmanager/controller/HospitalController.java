package com.hospitalmanager.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.entity.HospitalPlanLink;
import com.hospitalmanager.exception.AccessDeniedException;
import com.hospitalmanager.request.ClaimStatsDTO;
import com.hospitalmanager.service.ClaimAnalyticsService;
import com.hospitalmanager.service.HospitalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final ClaimAnalyticsService claimAnalyticsService;

    private Long getLoggedInUserId() {
        return Long.parseLong(
                (String) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        );
    }

    private String getLoggedInRole() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();
    }

    private void validateSelfOrAdmin(Long hosId) {

        Long loggedId = getLoggedInUserId();
        String role = getLoggedInRole();

        if (role.equals("ROLE_ADMIN"))
            return;

        if (role.equals("ROLE_HOSPITAL") && !loggedId.equals(hosId)) {
            throw new AccessDeniedException(
                    "You can only access your own hospital resources");
        }
    }

    // =============================
    // CLAIMS — hospital / admin / claim officer
    // =============================
    @GetMapping("/claims/{hosId}")
    public List<ClaimStatsDTO> getHospitalClaims(@PathVariable Long hosId){

        validateSelfOrAdmin(hosId);

        return claimAnalyticsService.getHospitalClaimsWithTotals(hosId);
    }

    // =============================
    // LINK PLAN — hospital self OR admin
    // =============================
    @PostMapping("/{hosId}/plans/{planId}/link")
    public String linkPlan(@PathVariable Long hosId,
                           @PathVariable Long planId){

        validateSelfOrAdmin(hosId);

        hospitalService.linkPlan(hosId, planId);
        return "Plan linked to hospital";
    }

    // =============================
    // UNLINK PLAN — hospital self OR admin
    // =============================
    @DeleteMapping("/{hosId}/plans/{planId}/unlink")
    public String unlinkPlan(@PathVariable Long hosId,
                             @PathVariable Long planId){

        validateSelfOrAdmin(hosId);

        hospitalService.unlinkPlan(hosId, planId);
        return "Plan removed from hospital";
    }

    // =============================
    // Everyone — view plans of hospital
    // =============================
    @GetMapping("/{hosId}/plans")
    public List<?> getHospitalPlans(@PathVariable Long hosId){
        return hospitalService.getHospitalPlans(hosId);
    }

    // =============================
    // Everyone — hospitals in network
    // =============================
    @GetMapping("/plan/{planId}/network-hospitals")
    public List<?> getNetworkHospitals(@PathVariable Long planId){
        return hospitalService.getHospitalsByPlan(planId);
    }

    // =============================
    // HOSPITAL ONLY — my own profile
    // =============================
    @GetMapping("/profile/me")
    public Hospital getMyProfile() {

        Long hospitalId = getLoggedInUserId();
        return hospitalService.getHospital(hospitalId);
    }
}
