package com.hospitalmanager.controller;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.exception.AccessDeniedException;
import com.hospitalmanager.request.ClaimStatsDTO;
import com.hospitalmanager.service.ClaimAnalyticsService;
import com.hospitalmanager.service.HospitalService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalControllerTest {

    @Mock
    private HospitalService hospitalService;

    @Mock
    private ClaimAnalyticsService claimAnalyticsService;

    @InjectMocks
    private HospitalController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuth(Long userId, String role) {
        var auth = new UsernamePasswordAuthenticationToken(
                String.valueOf(userId),
                "token",
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void hospital_can_fetch_own_claims() {

        mockAuth(10L, "ROLE_HOSPITAL");

        var dto = new ClaimStatsDTO();
        dto.setHospitalId(10L);

        when(claimAnalyticsService.getHospitalClaimsWithTotals(10L))
                .thenReturn(List.of(dto));

        var result = controller.getHospitalClaims(10L);

        assertEquals(1, result.size());
        verify(claimAnalyticsService).getHospitalClaimsWithTotals(10L);
    }

    @Test
    void hospital_cannot_access_other_hospital_claims() {

        mockAuth(10L, "ROLE_HOSPITAL");

        assertThrows(
                AccessDeniedException.class,
                () -> controller.getHospitalClaims(99L)
        );
    }

    @Test
    void admin_can_access_any_hospital_claims() {

        mockAuth(5L, "ROLE_ADMIN");

        controller.getHospitalClaims(77L);

        verify(claimAnalyticsService).getHospitalClaimsWithTotals(77L);
    }

    @Test
    void hospital_can_link_own_plan() {

        mockAuth(8L, "ROLE_HOSPITAL");

        controller.linkPlan(8L, 2L);

        verify(hospitalService).linkPlan(8L, 2L);
    }

    @Test
    void admin_can_link_any_hospital_plan() {

        mockAuth(1L, "ROLE_ADMIN");

        controller.linkPlan(9L, 2L);

        verify(hospitalService).linkPlan(9L, 2L);
    }

    @Test
    void unlink_plan_calls_service() {

        mockAuth(8L, "ROLE_HOSPITAL");

        controller.unlinkPlan(8L, 4L);

        verify(hospitalService).unlinkPlan(8L, 4L);
    }

    @Test
    void anyone_can_view_hospital_plans() {

        mockAuth(20L, "ROLE_CUSTOMER");

        controller.getHospitalPlans(5L);

        verify(hospitalService).getHospitalPlans(5L);
    }

    @Test
    void anyone_can_view_network_hospitals() {

        mockAuth(99L, "ROLE_CUSTOMER");

        controller.getNetworkHospitals(3L);

        verify(hospitalService).getHospitalsByPlan(3L);
    }

    @Test
    void hospital_can_fetch_own_profile() {

        mockAuth(7L, "ROLE_HOSPITAL");

        Hospital hospital = new Hospital();
        hospital.setId(7L);

        when(hospitalService.getHospital(7L)).thenReturn(hospital);

        var result = controller.getMyProfile();

        assertEquals(7L, result.getId());
        verify(hospitalService).getHospital(7L);
    }
}
