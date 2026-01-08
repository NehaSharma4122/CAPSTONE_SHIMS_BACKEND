package com.hospitalmanager.service;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.repository.HospitalRepository;
import com.hospitalmanager.request.HospitalOnboardingRequest;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalOnboardingServiceTest {

    @Mock
    HospitalRepository repo;

    @InjectMocks
    HospitalOnboardingService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        var auth = new UsernamePasswordAuthenticationToken(
                "9", "token", List.of()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void onboarding_creates_or_updates_profile() {

        HospitalOnboardingRequest req = new HospitalOnboardingRequest();
        req.setName("Apollo");
        req.setCity("Mumbai");
        req.setState("MH");
        req.setLicenseNumber("LIC88");
        req.setContactEmail("mail@test.com");
        req.setContactPhone("99999");

        when(repo.findById(9L)).thenReturn(Optional.empty());
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Hospital result = service.submitOnboarding(req);

        assertEquals(9L, result.getId());
        assertTrue(result.isProfileCompleted());
        assertEquals("Apollo", result.getName());

        verify(repo).save(any(Hospital.class));
    }

    @Test
    void isProfileCompleted_returns_true() {

        Hospital h = new Hospital();
        h.setProfileCompleted(true);

        when(repo.findById(9L)).thenReturn(Optional.of(h));

        assertTrue(service.isProfileCompleted(9L));
    }

    @Test
    void isProfileCompleted_returns_false_if_missing() {

        when(repo.findById(9L)).thenReturn(Optional.empty());

        assertFalse(service.isProfileCompleted(9L));
    }
}
