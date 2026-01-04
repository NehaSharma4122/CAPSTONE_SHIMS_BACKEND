package com.hospitalmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospitalmanager.entity.Hospital;
import com.hospitalmanager.entity.HospitalPlanLink;
import com.hospitalmanager.exception.ResourceNotFound;
import com.hospitalmanager.feign.PolicyFeignClient;
import com.hospitalmanager.repository.HospitalPlanRepository;
import com.hospitalmanager.repository.HospitalRepository;
import com.hospitalmanager.request.HospitalDTO;
import com.hospitalmanager.request.HospitalPlanResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepo;
    private final HospitalPlanRepository linkRepo;
    private final PolicyFeignClient policyFeignClient;

    private HospitalPlanResponseDTO mapToPlanDTO(HospitalPlanLink link) {

        HospitalDTO h = new HospitalDTO();
        h.setId(link.getHospital().getId());
        h.setName(link.getHospital().getName());
        h.setCity(link.getHospital().getCity());
        h.setState(link.getHospital().getState());

        HospitalPlanResponseDTO dto = new HospitalPlanResponseDTO();
        dto.setId(link.getId());
        dto.setPlanId(link.getPlanId());
        dto.setHospital(h);

        return dto;
    }

    public Hospital getHospital(Long id) {
        return hospitalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Hospital not found"));
    }

    public List<HospitalPlanResponseDTO> getHospitalPlans(Long id) {
        Hospital hospital = getHospital(id);
        return hospital.getLinkedPlans()
                .stream()
                .map(this::mapToPlanDTO)
                .toList();    
    }

    public void linkPlan(Long hosId, Long planId) {

        Hospital hospital = getHospital(hosId);
        boolean planExists = policyFeignClient.doesPlanExist(planId);
        if (linkRepo.existsByHospitalIdAndPlanId(hosId, planId)) {
            throw new IllegalStateException("Plan already linked to hospital");
        }

        if (!planExists) {
            throw new ResourceNotFound("Plan does not exist: " + planId);
        }
        HospitalPlanLink link = new HospitalPlanLink();
        link.setHospital(hospital);
        link.setPlanId(planId);
        
       

        linkRepo.save(link);
    }

    public void unlinkPlan(Long hosId, Long planId) {

        List<HospitalPlanLink> links =
                linkRepo.findByPlanId(planId)
                        .stream()
                        .filter(l -> l.getHospital().getId().equals(hosId))
                        .toList();

        linkRepo.deleteAll(links);
    }

    public List<HospitalPlanResponseDTO> getHospitalsByPlan(Long planId) {

        return linkRepo.findByPlanId(planId)
                .stream()
                .map(this::mapToPlanDTO)
                .toList();
    }    

}
