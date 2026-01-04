package com.hospitalmanager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.request.HospitalOnboardingRequest;
import com.hospitalmanager.service.HospitalOnboardingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalOnboardingController {

    private final HospitalOnboardingService onboardingService;

    @PostMapping("/onboarding")
    public ResponseEntity<?> completeOnboarding(
            @Valid @RequestBody HospitalOnboardingRequest request) {

        Hospital hospital = onboardingService.submitOnboarding(request);

        return ResponseEntity.ok(Map.of(
                "message", "Hospital profile completed successfully",
                "hospitalId", hospital.getId()
        ));
    }
}

