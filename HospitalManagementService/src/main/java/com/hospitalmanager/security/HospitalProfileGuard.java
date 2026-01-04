package com.hospitalmanager.security;

import org.springframework.stereotype.Component;

import com.hospitalmanager.exception.AccessDeniedException;
import com.hospitalmanager.service.HospitalOnboardingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HospitalProfileGuard {

    private final HospitalOnboardingService onboardingService;

    public void verifyProfile(Long userId, String role) {

        // Guard applies ONLY to hospitals
        if (!"ROLE_HOSPITAL".equals(role)) return;

        boolean completed = onboardingService.isProfileCompleted(userId);

        System.out.println("GUARD CHECK -> userId=" + userId + ", role=" + role);
        System.out.println("PROFILE COMPLETED = " + completed);

        if (!completed) {
            throw new AccessDeniedException(
                    "Complete hospital onboarding profile before accessing services");
        }
    }
}
