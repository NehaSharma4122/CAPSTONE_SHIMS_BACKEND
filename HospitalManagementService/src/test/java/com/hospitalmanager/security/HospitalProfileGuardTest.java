package com.hospitalmanager.security;

import com.hospitalmanager.exception.AccessDeniedException;
import com.hospitalmanager.service.HospitalOnboardingService;

import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalProfileGuardTest {

    @Mock
    HospitalOnboardingService onboardingService;

    @InjectMocks
    HospitalProfileGuard guard;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void non_hospital_role_is_ignored() {
        assertDoesNotThrow(() -> guard.verifyProfile(5L, "ROLE_ADMIN"));
    }

    @Test
    void hospital_with_completed_profile_passes_guard() {

        when(onboardingService.isProfileCompleted(8L)).thenReturn(true);

        assertDoesNotThrow(() -> guard.verifyProfile(8L, "ROLE_HOSPITAL"));
    }

    @Test
    void hospital_without_profile_throws_exception() {

        when(onboardingService.isProfileCompleted(9L)).thenReturn(false);

        assertThrows(
                AccessDeniedException.class,
                () -> guard.verifyProfile(9L, "ROLE_HOSPITAL")
        );
    }
}
