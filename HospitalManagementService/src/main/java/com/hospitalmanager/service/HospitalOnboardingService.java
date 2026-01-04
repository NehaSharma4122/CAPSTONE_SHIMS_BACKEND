package com.hospitalmanager.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.repository.HospitalRepository;
import com.hospitalmanager.request.HospitalOnboardingRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalOnboardingService {

    private final HospitalRepository hospitalRepo;

    private Long getLoggedInUserId() {
        return Long.parseLong(
                (String) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal()
        );
    }

    public Hospital submitOnboarding(HospitalOnboardingRequest req) {

        Long hospitalId = getLoggedInUserId();   // LOGIN ID = HOSPITAL ID

        Hospital hospital = hospitalRepo
                .findById(hospitalId)
                .orElse(new Hospital());

        hospital.setId(hospitalId);
        hospital.setProfileCompleted(true);

        hospital.setName(req.getName());
        hospital.setCity(req.getCity());
        hospital.setState(req.getState());
        hospital.setAddress(req.getAddress());
        hospital.setLicenseNumber(req.getLicenseNumber());
        hospital.setContactEmail(req.getContactEmail());
        hospital.setContactPhone(req.getContactPhone());

        return hospitalRepo.save(hospital);
    }

    public boolean isProfileCompleted(Long hospitalId) {
        return hospitalRepo.findById(hospitalId)
                .map(Hospital::isProfileCompleted)
                .orElse(false);
    }
}
