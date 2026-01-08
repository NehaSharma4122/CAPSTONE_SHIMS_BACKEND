package com.hospitalmanager.controller;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.request.HospitalOnboardingRequest;
import com.hospitalmanager.service.HospitalOnboardingService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalOnboardingControllerTest {

    @Mock
    HospitalOnboardingService onboardingService;

    @InjectMocks
    HospitalOnboardingController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onboarding_returns_success_response() {

        var req = new HospitalOnboardingRequest();
        req.setName("Apollo");

        Hospital hospital = new Hospital();
        hospital.setId(8L);

        when(onboardingService.submitOnboarding(req)).thenReturn(hospital);

        ResponseEntity<?> resp = controller.completeOnboarding(req);

        assertEquals(200, resp.getStatusCode().value());
        assertTrue(resp.getBody().toString().contains("hospitalId=8"));

        verify(onboardingService).submitOnboarding(req);
    }
}
