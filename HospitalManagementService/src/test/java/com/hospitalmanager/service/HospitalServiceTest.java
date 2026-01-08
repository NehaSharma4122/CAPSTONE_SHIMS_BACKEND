package com.hospitalmanager.service;

import com.hospitalmanager.entity.*;
import com.hospitalmanager.exception.ResourceNotFound;
import com.hospitalmanager.feign.PolicyFeignClient;
import com.hospitalmanager.repository.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HospitalServiceTest {

    @Mock HospitalRepository hospitalRepo;
    @Mock HospitalPlanRepository linkRepo;
    @Mock PolicyFeignClient policyFeign;

    @InjectMocks HospitalService service;

    Hospital hospital;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        hospital = new Hospital();
        hospital.setId(5L);
        hospital.setName("Apollo");
        hospital.setCity("Mumbai");
        hospital.setState("MH");
    }

    @Test
    void getHospital_returns_existing() {

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.of(hospital));

        assertEquals(hospital, service.getHospital(5L));
    }

    @Test
    void getHospital_throws_if_missing() {

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class,
                () -> service.getHospital(5L));
    }

    @Test
    void linkPlan_succeeds_when_valid() {

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.of(hospital));

        when(policyFeign.doesPlanExist(10L)).thenReturn(true);
        when(linkRepo.existsByHospitalIdAndPlanId(5L, 10L))
                .thenReturn(false);

        service.linkPlan(5L, 10L);

        verify(linkRepo).save(any(HospitalPlanLink.class));
    }

    @Test
    void linkPlan_prevents_duplicate() {

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.of(hospital));

        when(policyFeign.doesPlanExist(10L)).thenReturn(true);
        when(linkRepo.existsByHospitalIdAndPlanId(5L, 10L))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> service.linkPlan(5L, 10L));
    }

    @Test
    void linkPlan_throws_if_plan_missing() {

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.of(hospital));

        when(policyFeign.doesPlanExist(10L)).thenReturn(false);

        assertThrows(ResourceNotFound.class,
                () -> service.linkPlan(5L, 10L));
    }

    @Test
    void unlinkPlan_deletes_matching_links() {

        HospitalPlanLink link = new HospitalPlanLink();
        link.setHospital(hospital);

        when(linkRepo.findByPlanId(10L))
                .thenReturn(List.of(link));

        service.unlinkPlan(5L, 10L);

        verify(linkRepo).deleteAll(any());
    }

    @Test
    void getHospitalPlans_maps_dto() {

        HospitalPlanLink link = new HospitalPlanLink();
        link.setId(1L);
        link.setPlanId(3L);
        link.setHospital(hospital);

        hospital.setLinkedPlans(List.of(link));

        when(hospitalRepo.findById(5L))
                .thenReturn(Optional.of(hospital));

        var result = service.getHospitalPlans(5L);

        assertEquals(1, result.size());
        assertEquals("Apollo", result.get(0).getHospital().getName());
    }

    @Test
    void getHospitalsByPlan_maps_list() {

        HospitalPlanLink link = new HospitalPlanLink();
        link.setId(2L);
        link.setPlanId(7L);
        link.setHospital(hospital);

        when(linkRepo.findByPlanId(7L))
                .thenReturn(List.of(link));

        var result = service.getHospitalsByPlan(7L);

        assertEquals(1, result.size());
        assertEquals(7L, result.get(0).getPlanId());
    }
}
